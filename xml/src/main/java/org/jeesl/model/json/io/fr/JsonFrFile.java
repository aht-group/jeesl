package org.jeesl.model.json.io.fr;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.model.json.system.status.JsonType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@JsonRootName(value="frFile")
public class JsonFrFile implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("name")
	private String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@JsonProperty("category")
	private String category;
	public String getCategory() {return category;}
	public void setCategory(String category) {this.category = category;}
	
	@JsonProperty("type")
	private JsonType type;
	public JsonType getType() {return type;}
	public void setType(JsonType type) {this.type = type;}

	@JsonProperty("size")
	private Long size;
	public Long getSize() {return size;}
	public void setSize(Long size) {this.size = size;}
	
	@JsonProperty("start")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime ldtRecord;
	public LocalDateTime getLdtRecord() {return ldtRecord;}
	public void setLdtRecord(LocalDateTime ldtRecord) {this.ldtRecord = ldtRecord;}

	@JsonProperty("content")
	private byte[] content;
	public byte[] getContent() {return content;}
	public void setContent(byte[] content) {this.content = content;}
}