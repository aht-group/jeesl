package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="host")
public class JsonSsiHost implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("ip")
	private String ip;
	public String getIp() {return ip;}
	public void setIp(String ip) {this.ip = ip;}
}