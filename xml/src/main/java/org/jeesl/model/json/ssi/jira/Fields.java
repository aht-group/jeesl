package org.jeesl.model.json.ssi.jira;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="fields")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("summary")
	private String summary;
	public String getSummary() {return summary;}
	public void setSummary(String summary) {this.summary = summary;}

	@JsonProperty("status")
	private Status status;
	public Status getStatus() {return status;}
	public void setStatus(Status status) {this.status = status;}
	
	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	
	@JsonProperty("comment")
	private Comment comment;
	public Comment getComment() {return comment;}
	public void setComment(Comment comment) {this.comment = comment;}
	
}