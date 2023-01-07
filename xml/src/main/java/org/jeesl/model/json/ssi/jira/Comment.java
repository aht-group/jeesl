package org.jeesl.model.json.ssi.jira;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="comment")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("comments")
	private List<Comments> comments;
	public List<Comments> getComments() {return comments;}
	public void setComments(List<Comments> comments) {this.comments = comments;}
}
