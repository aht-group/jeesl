package org.jeesl.model.json.io.maven;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="dependency")
public class JsonMavenDependency implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("from")
	private Long from;
	public Long getFrom() {return from;}
	public void setFrom(Long from) {this.from = from;}

	@JsonProperty("to")
	private String to;
	public String getTo() {return to;}
	public void setTo(String to) {this.to = to;}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}