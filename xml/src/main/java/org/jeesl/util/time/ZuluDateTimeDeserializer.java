package org.jeesl.util.time;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

public class ZuluDateTimeDeserializer extends JsonDeserializer<LocalDateTime>
{
	// Allows to parse incomping data in zulu format
//	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
//	@JsonSerialize(using = LocalDateTimeSerializer.class)
//	@JsonDeserialize(using = ZuluDateTimeDeserializer.class)
	
	@Override public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctx) throws IOException,JsonProcessingException
	{
	    ObjectCodec oc = jp.getCodec();
	    TextNode node = (TextNode) oc.readTree(jp);
	    String dateString = node.textValue();
	    return LocalDateTime.parse(dateString, java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);
	}
}