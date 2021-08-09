package org.jeesl.model.json.system.io.ssi.mobile;

import java.io.Serializable;

import org.jeesl.model.json.system.security.JsonSecurityUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="login")
public class Login implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("success")
	private Boolean success;
	public Boolean isSuccess() {return success;}
	public void setSuccess(Boolean success) {this.success = success;}
	
	@JsonProperty("user")
	private JsonSecurityUser user;
	public JsonSecurityUser getUser() {return user;}
	public void setUser(JsonSecurityUser user) {this.user = user;}
	
	@JsonProperty("username")
	private String username;
	public String getUsername() {return username;}
	public void setUsername(String username) {this.username = username;}
	
	@JsonProperty("password")
	private String password;
	public String getPassword() {return password;}
	public void setPassword(String password) {this.password = password;}
}