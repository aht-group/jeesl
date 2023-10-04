package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="crendential")
public class JsonSsiCredential implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("system")
	private JsonSsiSystem system;
	public JsonSsiSystem getSystem() {return system;}
	public void setSystem(JsonSsiSystem system) {this.system = system;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("host")
	private String host;
	public String getHost() {return host;}
	public void setHost(String host) {this.host = host;}
	
	@JsonProperty("port")
	private Integer port;
	public Integer getPort() {return port;}
	public void setPort(Integer port) {this.port = port;}
	
	@JsonProperty("token")
	private String token;
	public String getToken() {return token;}
	public void setToken(String token) {this.token = token;}
	
	@JsonProperty("url")
	private String url;
	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}
	
	@JsonProperty("user")
	private String user;
	public String getUser() {return user;}
	public void setUser(String user) {this.user = user;}

	@JsonProperty("pwd")
	private String password;
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
}