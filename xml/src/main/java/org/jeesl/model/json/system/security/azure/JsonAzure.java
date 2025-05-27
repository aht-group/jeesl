package org.jeesl.model.json.system.security.azure;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="azure")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAzure implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonAzure() {}
	
	@JsonProperty("users")
	private List<JsonAzureUser> users;
	public List<JsonAzureUser> getUsers() {return users;}
	public void setUsers(List<JsonAzureUser> users) {this.users = users;}
}