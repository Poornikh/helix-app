package com.helix.app.controller;

import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helix.app.dao.ProductDAO;
import com.helix.app.model.Event;
import com.helix.app.util.HelixUtil;

@RestController
@RequestMapping("events")
public class EventController  {
	
	private org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductDAO productDao;
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.GET, value ="/get" ,produces={MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public String getEvent(@RequestParam String eventId) throws JsonProcessingException {
		ObjectMapper mapper = HelixUtil.getObjectMapper();
		LOG.info("Received eventId {} ",eventId);
		return mapper.writeValueAsString(productDao.getEvent(eventId)); 
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.POST, value ="/save", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})	
	public ResponseEntity<Object> saveEvent(@Valid @RequestBody Event event) {
		LOG.info("Received Event {} ",event.toString());
		productDao.saveEvents(event);
		LOG.info("Returning Success ");
		return new ResponseEntity<Object>("{action:success}", HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(method=RequestMethod.DELETE, value ="/delete", produces={MediaType.APPLICATION_JSON_UTF8_VALUE})	
	public void deleteEvent(@RequestParam String eventId) {	
		productDao.deleteEvent(eventId);
	}
	
	

}
