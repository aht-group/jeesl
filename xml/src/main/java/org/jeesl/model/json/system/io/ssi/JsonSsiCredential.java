package org.jeesl.model.json.system.io.ssi;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="crendential")
public class JsonSsiCredential implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("user")
	private String user;
	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;}

	@JsonProperty("pwd")
	private String password;
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
	
	@JsonProperty("host")
	private String host;
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}

	@JsonProperty("url")
	private String url;
	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}
}