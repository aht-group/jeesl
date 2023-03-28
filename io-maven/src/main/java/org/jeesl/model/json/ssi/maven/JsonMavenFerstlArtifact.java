package org.jeesl.model.json.ssi.maven;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

//@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="artifact")
public class JsonMavenFerstlArtifact implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("id")
	private String id;
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}

	@JsonProperty("numericId")
	private Long numericId;
	public Long getNumericId() {return numericId;}
	public void setNumericId(Long numericId) {this.numericId = numericId;}

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
	
	@JsonProperty("optional")
	private Boolean optional;
	public Boolean getOptional() {return optional;}
	public void setOptional(Boolean optional) {this.optional = optional;}
	
	@JsonProperty("scopes")
	private List<String> scopes;
	public List<String> getScopes() {return scopes;}
	public void setScopes(List<String> scopes) {this.scopes = scopes;}
	
	@JsonProperty("types")
	private List<String> types;
	public List<String> getTypes() {return types;}
	public void setTypes(List<String> types) {this.types = types;}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}