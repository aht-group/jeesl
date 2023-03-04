package org.jeesl.model.json.io.ssi.core;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="system")
public class JsonSsiSystem implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("credentials")
	private List<JsonSsiCredential> credentials;
	public List<JsonSsiCredential> getCredentials() {return credentials;}
	public void setCredentials(List<JsonSsiCredential> credentials) {this.credentials = credentials;}
}