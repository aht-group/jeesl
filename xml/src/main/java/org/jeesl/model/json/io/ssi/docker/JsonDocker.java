package org.jeesl.model.json.io.ssi.docker;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonRootName(value="")
public class JsonDocker implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("Containers")
	private List<JsonDockerContainer> containers;
	public List<JsonDockerContainer> getContainers() {return containers;}
	public void setContainers(List<JsonDockerContainer> containers) {this.containers = containers;}

	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}	
}

