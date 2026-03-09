package org.jeesl.util.time;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public final class UnixTimestampSerializer
{
	private UnixTimestampSerializer() {}
	
	public static final class Serializer extends JsonSerializer<Instant> {

	    @Override
	    public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException
	    {
	        gen.writeNumber(value.getEpochSecond());
	    }
	}

	public static final class Deserializer extends JsonDeserializer<Instant>
	{

	    @Override
	    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
	        return Instant.ofEpochSecond(p.getLongValue());
	    }
	}
}