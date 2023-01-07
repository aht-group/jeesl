package org.jeesl.model.json.io.ssi.mobile;

import java.io.Serializable;

import org.jeesl.model.json.system.status.JsonStatus;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="version")
public class Version implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("serverVersion")
	private String serverVersion;
	public String getServerVersion() {return serverVersion;}
	public void setServerVersion(String serverVersion) {this.serverVersion = serverVersion;}
	
	@JsonProperty("mobileVersion")
	private String mobileVersion;
	public String getMobileVersion() {return mobileVersion;}
	public void setMobileVersion(String mobileVersion) {this.mobileVersion = mobileVersion;}
	
	@JsonProperty("status")
	private JsonStatus status;
	public JsonStatus getStatus() {return status;}
	public void setStatus(JsonStatus status) {this.status = status;}
}