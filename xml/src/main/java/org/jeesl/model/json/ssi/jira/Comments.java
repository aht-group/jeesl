package org.jeesl.model.json.ssi.jira;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="comments")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comments implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("body")
	private String body;
	public String getBody() {return body;}
	public void setBody(String body) {this.body = body;}
	
	@JsonProperty("author")
	private Author author;
	public Author getAuthor() {return author;}
	public void setAuthor(Author author) {this.author = author;}
}
