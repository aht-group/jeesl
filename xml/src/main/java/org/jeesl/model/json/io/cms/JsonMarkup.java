package org.jeesl.model.json.io.cms;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="markup")
public class JsonMarkup implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}
	
	@JsonProperty("lkey")
	private String lkey;
	public String getLkey() {return lkey;}
	public void setLkey(String lkey) {this.lkey = lkey;}
	
	@JsonProperty("content")
	private String content;
	public String getContent() {return content;}
	public void setContent(String content) {this.content = content;}
	

}