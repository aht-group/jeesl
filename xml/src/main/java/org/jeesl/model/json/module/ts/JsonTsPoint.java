package org.jeesl.model.json.module.ts;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="value")
public class JsonTsPoint implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	@JsonIgnore public boolean isSetId() {return id!=null;}
	
	@JsonProperty("data")
	private JsonTsData data;
	public JsonTsData getData() {return data;}
	public void setData(JsonTsData data) {this.data = data;}

	@JsonProperty("mp")
	private JsonTsMultipoint mp;
	public JsonTsMultipoint getMp() {return mp;}
	public void setMp(JsonTsMultipoint mp) {this.mp = mp;}
	@JsonIgnore public boolean isSetMp() {return mp!=null;}
	
	@JsonProperty("value")
	private Double value;
	public Double getValue() {return value;}
	public void setValue(Double value) {this.value = value;}
	@JsonIgnore public boolean isSetValue() {return value!=null;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}