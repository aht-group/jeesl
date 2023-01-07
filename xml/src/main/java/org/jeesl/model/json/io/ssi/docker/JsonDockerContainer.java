package org.jeesl.model.json.io.ssi.docker;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
//@JsonRootName(value="")
public class JsonDockerContainer implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("Id")
	private String id;
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}

	@JsonProperty("Names")
	private String[] names;
	public String[] getNames() {return names;}
	public void setNames(String[] names) {this.names = names;}

	@JsonProperty("Image")
	private String image;
	public String getImage() {return image;}
	public void setImage(String image) {this.image = image;}

//	@JsonProperty("Labels")
//	private String[] labels;
//	public String[] getLabels() {return labels;}
//	public void setLabels(String[] labels) {this.labels = labels;}
		
	@JsonProperty("State")
	private String state;
	public String getState() {return state;}
	public void setState(String state) {this.state = state;}
	
	@JsonProperty("Status")
	private String status;
	public String getStatus() {return status;}
	public void setStatus(String status) {this.status = status;}
	
	@JsonProperty("Ports")
	private List<JsonDockerContainerPorts> ports;
	public List<JsonDockerContainerPorts> getPorts() {return ports;}
	public void setPorts(List<JsonDockerContainerPorts> ports) {this.ports = ports;}
	
	@JsonProperty("Mounts")
	private List<JsonDockerContainerMounts> mounts;
	public List<JsonDockerContainerMounts> getMounts() {return mounts;}
	public void setMounts(List<JsonDockerContainerMounts> mounts) {this.mounts = mounts;}
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}	
}