package com.helix.app;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class GetActionControllerTest {
	
	@Autowired
    private MockMvc mvc;
 
    @Autowired
    private ProductDAO repository;
    
    @Autowired
    private DataSource datasource;
    
    @Test
    public void testEventGetAction_PositiveCase() throws Exception{
    	JSONObject evt = new JSONObject();
        evt.put("id", "1");
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
        
        JSONObject evt1 = new JSONObject();
        evt1.put("id", "1");
        evt1.put("timestamp", "2018-08-21T04:55:11Z");
        JSONObject prod1 = new JSONObject();
        prod1.put("id", 1l); 
        JSONArray array1 = new JSONArray();
        array1.put(prod1);
        
        evt1.put("products", array1);
    	
    mvc.perform(get("/events/get?eventId=1")
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(content().json(evt1.toString()))
            .andExpect(jsonPath("$.id", is("1")))
            .andExpect(jsonPath("$.products[0].id", is(1)));
    }
    
    @Test
    public void testEventGetAction_InvalidEventId() throws Exception{
    	
    mvc.perform(get("/events/get?eventId=4")
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Event doesn't Exists.")));
        
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
