package org.jeesl.model.json.io.db.pg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDateTruncation implements Serializable
{
	public static final long serialVersionUID=1;
	
	
	
	@JsonProperty("alias")
	private String alias;
	public String getAlias() {return alias;}
	public void setAlias(String alias) {this.alias = alias;}
	
	@JsonProperty("attribute")
	private String attribute;
	public String getAttribute() {return attribute;}
	public void setAttribute(String attribute) {this.attribute = attribute;}

	@JsonProperty("interval")
	private String interval;
	public String getInterval() {return interval;}
	public void setInterval(String interval) {this.interval = interval;}
	
}