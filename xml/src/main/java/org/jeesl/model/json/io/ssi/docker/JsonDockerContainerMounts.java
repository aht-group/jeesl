package org.jeesl.model.json.io.ssi.docker;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="Ports")
public class JsonDockerContainerMounts implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("Source")
	private String source;
	public String getSource() {return source;}
	public void setSource(String source) {this.source = source;}

	@JsonProperty("Destination")
	private String destination;
	public String getDestination() {return destination;}
	public void setDestination(String destination) {this.destination = destination;}

	@JsonProperty("Mode")
	private String mode;
	public String getMode() {return mode;}
	public void setMode(String mode) {this.mode = mode;}
	
	@JsonProperty("Type")
	private String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}
	
	@JsonProperty("RW")
	private boolean rw;
	public boolean isRw() {return rw;}
	public void setRw(boolean rw) {this.rw = rw;}
	
	@JsonProperty("Propagation")
	private String propagation;
	public String getPropagation() {return propagation;}
	public void setPropagation(String propagation) {this.propagation = propagation;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}

