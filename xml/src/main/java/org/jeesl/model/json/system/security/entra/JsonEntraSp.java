package org.jeesl.model.json.system.security.entra;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonEntraSp implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonEntraSp() {}
	
	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("code")
	public String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}
	
	@JsonProperty("name")
	public String name;
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	
	@JsonProperty("directory")
	public String directory;
	public String getDirectory() {return directory;}
	public void setDirectory(String directory) {this.directory = directory;}
	
	@JsonProperty("context")
	public String context;
	public String getContext() {return context;}
	public void setContext(String context) {this.context = context;}
	
	@JsonProperty("scope")
	public String scope;
	public String getScope() {return scope;}
	public void setScope(String scope) {this.scope = scope;}
}