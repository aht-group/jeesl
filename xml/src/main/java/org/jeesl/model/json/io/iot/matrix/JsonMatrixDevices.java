package org.jeesl.model.json.io.iot.matrix;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="devices")
public class JsonMatrixDevices implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("devices")
	private List<JsonMatrixDevice> devices;
	public List<JsonMatrixDevice> getDevices() {return devices;}
	public void setDevices(List<JsonMatrixDevice> devices) {this.devices = devices;}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}