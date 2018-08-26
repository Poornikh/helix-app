package com.helix.app.custom;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomeDateSerializer extends StdSerializer<ZonedDateTime> {

	private org.slf4j.Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	
	private static final DateTimeFormatter TO_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("UTC"));
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public CustomeDateSerializer() 
    {
        this(null);
    }
    
    
	public CustomeDateSerializer(Class<ZonedDateTime> vc) {
        super((Class<ZonedDateTime>) vc);
    }
	
	@Override
	public void serialize(ZonedDateTime value, JsonGenerator gen,
			SerializerProvider provider) throws IOException {
		
	
		LOG.info("Serialized To {} ", value.withZoneSameInstant(ZoneId.of("UTC")));
		gen.writeString(value.format(TO_FORMAT));
	}

}
