package org.jeesl.model.json.module.ts;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="scope")
public class JsonTsScope implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("type")
	private JsonType type;
	public JsonType getType() {return type;}
	public void setType(JsonType type) {this.type = type;}
	
	@JsonProperty("label")
	private String label;
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}