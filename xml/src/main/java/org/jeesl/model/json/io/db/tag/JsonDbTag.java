package org.jeesl.model.json.io.db.tag;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="tag")
public class JsonDbTag implements Serializable
{
	public static final long serialVersionUID=1;
	
//	public enum Agg{count,sum,min,avg,max}
	
	public JsonDbTag() {}
	
	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
}