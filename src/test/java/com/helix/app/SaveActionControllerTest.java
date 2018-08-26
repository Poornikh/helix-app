package com.helix.app;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.helix.app.dao.ProductDAO;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.MOCK,  classes = HelixAppApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class SaveActionControllerTest {
	
	@Autowired
    private MockMvc mvc;
 
    @Autowired
    private ProductDAO repository;
    
    @Autowired
    private DataSource datasource;
    
    @Test
    public void testEventSaveAction_PositiveCase() throws Exception{
       
    JSONObject evt = new JSONObject();
    evt.put("id", "7");
    evt.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod = new JSONObject();
    prod.put("id", 1l);
    prod.put("name", "CAP");
    prod.put("quantity", 1);
    prod.put("sale_amount", 1.0d);
    
    JSONArray array = new JSONArray();
    array.put(prod);
    
    evt.put("products", array);
  
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content(evt.toString()))
        .andExpect(status().isOk())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.action", is("success")));
    }
    
    @Test
    public void testEventSaveAction_PositiveCaseWithSameProductId() throws Exception{
    JSONObject evt1 = new JSONObject();
    evt1.put("id", "2");
    evt1.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod1 = new JSONObject();
    prod1.put("id", 1l); //SAME PRODUCT ID IS ALLOWED AS PRODUCT HAS COMPOSITE KEYS
    prod1.put("name", "CAP");
    prod1.put("quantity", 1);
    prod1.put("sale_amount", 1.0d);
    
    JSONArray array1 = new JSONArray();
    array1.put(prod1);
    
    evt1.put("products", array1);
    
    mvc.perform(post("/events/save")
        	.contentType(MediaType.APPLICATION_JSON)
        	.content(evt1.toString()))
            .andExpect(status().isOk())
            .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.action", is("success")));
    }
    
    @Test
    public void testEventSaveAction_EventExistsException() throws Exception{
    JSONObject evt1 = new JSONObject();
    evt1.put("id", "2");
    evt1.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod1 = new JSONObject();
    prod1.put("id", 1l); //SAME PRODUCT ID IS ALLOWED AS PRODUCT HAS COMPOSITE KEYS
    prod1.put("name", "CAP");
    prod1.put("quantity", 1);
    prod1.put("sale_amount", 1.0d);
    
    JSONArray array1 = new JSONArray();
    array1.put(prod1);
    
    evt1.put("products", array1);
    
    mvc.perform(post("/events/save")
        	.contentType(MediaType.APPLICATION_JSON)
        	.content(evt1.toString()))
            .andExpect(status().is5xxServerError())
            .andExpect(content()
            .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Event Exists With same event Id " + "2" + ", Provide a Alternate Event Id")));
    }
    
    
    @Test
    public void testEventSaveAction_InvalidJson() throws Exception{
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content("\"id\":\"2\" \"desc:\" \"Invalid Input\""))
        .andExpect(status().is4xxClientError())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Invalid Json Format")));
    }
    
    
    @Test
    public void testEventSaveAction_InvalidDateFormat() throws Exception{
    JSONObject evt = new JSONObject();
    evt.put("id", "1");
    evt.put("timestamp", "2018-08-21T12:+08:00");
    JSONObject prod = new JSONObject();
    prod.put("id", 1l);
    prod.put("name", "CAP");
    prod.put("quantity", 1);
    prod.put("sale_amount", 1.0d);
      
    JSONArray array = new JSONArray();
    array.put(prod);
    	    
    evt.put("products", array);
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content(evt.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Invalid Date Format, Please Provide in Format yyyy-MM-ddTHH:mm:ss[+\\-]xx:xx")));
    }
    
    
    @Test
    public void testEventSaveAction_NoEventIdInJson() throws Exception{
    JSONObject evt = new JSONObject();  
    evt.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod = new JSONObject();
    prod.put("id", 1l);
    prod.put("name", "CAP");
    prod.put("quantity", 1);
    prod.put("sale_amount", 1.0d);
      
    JSONArray array = new JSONArray();
    array.put(prod);
    	    
    evt.put("products", array);
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content(evt.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Validation Failed")));
    }
    
    @Test
    public void testEventSaveAction_NoProductNameJson() throws Exception{
    JSONObject evt = new JSONObject();
    evt.put("id", "1");
    evt.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod = new JSONObject();
    prod.put("id", 1l);   
    prod.put("quantity", 1);
    prod.put("sale_amount", 1.0d);
      
    JSONArray array = new JSONArray();
    array.put(prod);
    	    
    evt.put("products", array);
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content(evt.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Validation Failed")));
    }
    
    @Test
    public void testEventSaveAction_NoProductIdJson() throws Exception{
    JSONObject evt = new JSONObject();
    evt.put("id", "1");
    evt.put("timestamp", "2018-08-21T12:55:11+08:00");
    JSONObject prod = new JSONObject();
    prod.put("name", "CAP");
    prod.put("quantity", 1);
    prod.put("sale_amount", 1.0d);
      
    JSONArray array = new JSONArray();
    array.put(prod);
    	    
    evt.put("products", array);
    mvc.perform(post("/events/save")
    	.contentType(MediaType.APPLICATION_JSON)
    	.content(evt.toString()))
        .andExpect(status().is4xxClientError())
        .andExpect(content()
        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Validation Failed")));
    }
    
    @After
    public void tearDown() {
        try {
            clearDatabase();
        } catch (Exception e) {
          
        }
    }


    public void clearDatabase() throws Exception {
      
      Connection connection = null;
      try {
        connection = datasource.getConnection();
        try {
          Statement stmt = connection.createStatement();
          try {
            stmt.execute("TRUNCATE SCHEMA PUBLIC AND COMMIT");
            connection.commit();
          } finally {
            stmt.close();
          }
        } catch (SQLException e) {
            connection.rollback();
            throw new Exception(e);
        }
        } catch (SQLException e) {
            throw new Exception(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
   
}
