package com.helix.app.util;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.helix.app.custom.CustomDateDeserializer;
import com.helix.app.custom.CustomeDateSerializer;

public class HelixUtil {
	
	public static ObjectMapper getObjectMapper(){
		ObjectMapper mapper = new ObjectMapper();		
		JavaTimeModule module = new JavaTimeModule();
		CustomDateDeserializer deserializer = new CustomDateDeserializer();
		module.addDeserializer(ZonedDateTime.class, deserializer);
		module.addSerializer(ZonedDateTime.class, new CustomeDateSerializer());
		mapper.registerModule(module);
		return mapper;
	}

}
