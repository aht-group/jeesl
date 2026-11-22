package org.jeesl.model.json.system.security.collection;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.security.JsonSecurityRole;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value="roles")
public class JsonSecurityRoleCollection implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("roles")
	private List<JsonSecurityRole> roles;
	public List<JsonSecurityRole> getRoles() {return roles;}
	public void setRoles(List<JsonSecurityRole> roles) {this.roles = roles;}
}
