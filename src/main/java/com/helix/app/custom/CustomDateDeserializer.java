package com.helix.app.custom;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.helix.app.exception.InvalidDateFormatException;


public class CustomDateDeserializer extends StdDeserializer<ZonedDateTime> {
	 
	private org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
    
    private static final DateTimeFormatter FROM_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    

    public CustomDateDeserializer() {
        this(null);
    }
 
    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }
 
    @Override
    public ZonedDateTime deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException ,JsonProcessingException
       {
        	String date = jsonparser.getText();
        	if(!date.matches("^\\d{4}-[0-1]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d:[0-5]\\d[+-]\\d\\d:\\d\\d$")){
        		throw new InvalidDateFormatException("Invalid Date Format, Please Provide in Format yyyy-MM-ddTHH:mm:ss[+\\-]xx:xx");
        	}
        	ZonedDateTime convertedDateTime = null;
        	LOG.info("Received Date {} " , date); 
        	try {
        	OffsetDateTime receievedDateTime = OffsetDateTime.parse(date, FROM_FORMAT);
        	convertedDateTime = receievedDateTime.withOffsetSameInstant(ZoneOffset.UTC).toZonedDateTime();
        	}catch(Exception e) {
        		LOG.error("Error on Date Parsing {} "  ,date, e); 
        		new DateTimeParseException("Date Parse Exception, Please Provide in Format yyyy-MM-ddTHH:mm:ss[+\\-]xx:xx", date,0);
        	}
        	LOG.info("Deserialized {} to {} " , date, convertedDateTime.toString());
            return convertedDateTime;
      
    }

}

