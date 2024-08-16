package org.jeesl.model.json.io.ssi.mqtt;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="message")
public class JsonMqttMessage implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("topic")
	private String topic;
	public String getTopic() {return topic;}
	public void setTopic(String topic) {this.topic = topic;}
	
	@JsonProperty("time")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime time;
	public LocalDateTime getTime() {return time;}
	public void setTime(LocalDateTime time) {this.time = time;}
	
	@JsonProperty("payload")
	private String payload;
	public String getPayload() {return payload;}
	public void setPayload(String payload) {this.payload = payload;}
	

	
}