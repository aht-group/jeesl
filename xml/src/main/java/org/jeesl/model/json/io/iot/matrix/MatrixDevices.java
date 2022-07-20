package org.jeesl.model.json.io.iot.matrix;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="devices")
public class MatrixDevices implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("devices")
	private List<MatrixDevice> devices;
	public List<MatrixDevice> getDevices() {return devices;}
	public void setDevices(List<MatrixDevice> devices) {this.devices = devices;}


	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}