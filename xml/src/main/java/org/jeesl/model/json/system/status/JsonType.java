package org.jeesl.model.json.system.status;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="type")
public class JsonType implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonType() {}
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("position")
	private Integer position;
	public Integer getPosition() {return position;}
	public void setPosition(Integer position) {this.position = position;}

	@JsonProperty("label")
	private String label;
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		return sb.toString();
	}
}