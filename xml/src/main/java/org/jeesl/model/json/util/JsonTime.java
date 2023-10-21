package org.jeesl.model.json.util;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.json.bind.annotation.JsonbDateFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonRootName(value="filter")
public class JsonTime implements Serializable
{
	public static final long serialVersionUID=1;
	public enum Type{idList,idSingle,date}
	
//	private JsonTime() {}
	
	@JsonProperty("ldt1")
	@JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime ldt1;
	public LocalDateTime getLdt1() {return ldt1;}
	public void setLdt1(LocalDateTime ldt1) {this.ldt1 = ldt1;}
	
	@JsonProperty("ldt2")
	@JsonbDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using=LocalDateTimeDeserializer.class)
	@JsonSerialize(using=LocalDateTimeSerializer.class)
	private LocalDateTime ldt2;
	public LocalDateTime getLdt2() {return ldt2;}
	public void setLdt2(LocalDateTime ldt2) {this.ldt2 = ldt2;}

	public String toString()
	{
		StringBuffer sb = new StringBuffer();		
		return sb.toString();
	}
}