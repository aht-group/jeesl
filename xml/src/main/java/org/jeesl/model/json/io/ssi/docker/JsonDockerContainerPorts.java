package org.jeesl.model.json.io.ssi.docker;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="Ports")
public class JsonDockerContainerPorts implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("IP")
	private String ip;
	public String getIp() {return ip;}
	public void setIp(String ip) {this.ip = ip;}

	@JsonProperty("PrivatePort")
	private String privatePort;
	public String getPrivatePort() {return privatePort;}	
	public void setPrivatePort(String privatePort) {this.privatePort = privatePort;}


	@JsonProperty("PublicPort")
	private String publicPort;
	public String getPublicPort() {return publicPort;}
	public void setPublicPort(String publicPort) {this.publicPort = publicPort;}

	@JsonProperty("Type")
	private String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}	
	
	@Override public String toString()
	{
		StringBuffer sb = new StringBuffer();	
		return sb.toString();
	}
}
