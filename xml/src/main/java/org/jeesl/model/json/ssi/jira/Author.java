package org.jeesl.model.json.ssi.jira;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="author")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("displayName")
	private String displayName;
	public String getDisplayName() {return displayName;}
	public void setDisplayName(String displayName) {this.displayName = displayName;}
}
