package org.jeesl.model.json.io.maven;

import java.io.Serializable;
import java.util.List;

import org.jeesl.model.json.system.status.JsonScope;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="artifact")
public class JsonMavenArtifact implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("groupId")
	private String groupId;
	public String getGroupId() {return groupId;}
	public void setGroupId(String groupId) {this.groupId = groupId;}
	
	@JsonProperty("artifactId")
	private String artifactId;
	public String getArtifactId() {return artifactId;}
	public void setArtifactId(String artifactId) {this.artifactId = artifactId;}
	
	@JsonProperty("version")
	private String version;
	public String getVersion() {return version;}
	public void setVersion(String version) {this.version = version;}
	
	@JsonProperty("scopes")
	private List<JsonScope> scopes;
	public List<JsonScope> getScopes() {return scopes;}
	public void setScopes(List<JsonScope> scopes) {this.scopes = scopes;}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}