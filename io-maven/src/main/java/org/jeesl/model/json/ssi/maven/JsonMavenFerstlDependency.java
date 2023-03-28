package org.jeesl.model.json.ssi.maven;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

//@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="artifact")
public class JsonMavenFerstlDependency implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("from")
	private String from;
	public String getFrom() {return from;}
	public void setFrom(String from) {this.from = from;}

	@JsonProperty("to")
	private String to;
	public String getTo() {return to;}
	public void setTo(String to) {this.to = to;}

	@JsonProperty("numericFrom")
	private Long numericFrom;
	public Long getNumericFrom() {return numericFrom;}
	public void setNumericFrom(Long numericFrom) {this.numericFrom = numericFrom;}

	@JsonProperty("numericTo")
	private String numericTo;
	public String getNumericTo() {return numericTo;}
	public void setNumericTo(String numericTo) {this.numericTo = numericTo;}

	
	
	@JsonProperty("resolution")
	private String resolution;
	public String getResolution() {return resolution;}
	public void setResolution(String resolution) {this.resolution = resolution;}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}