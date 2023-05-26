package org.jeesl.model.json.io.maven;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="graph")
public class JsonMavenGraph implements Serializable
{
	public static final long serialVersionUID=1;


	@JsonProperty("id")
	private Long id;
	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	@JsonProperty("code")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("label")
	private String label;
	public String getLabel() {return label;}
	public void setLabel(String label) {this.label = label;}

	@JsonProperty("description")
	private String description;
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}

	@JsonProperty("artifacts")
	private List<JsonMavenArtifact> artifacts;
	public List<JsonMavenArtifact> getArtifacts() {return artifacts;}
	public void setArtifacts(List<JsonMavenArtifact> artifacts) {this.artifacts = artifacts;}

	@JsonProperty("dependencies")
	private List<JsonMavenDependency> dependencies;
	public List<JsonMavenDependency> getDependencies() {return dependencies;}	
	public void setDependencies(List<JsonMavenDependency> dependencies) {this.dependencies = dependencies;}

	@JsonProperty("fonts")
	private List<JsonFont> fonts;
	public List<JsonFont> getFonts() {return fonts;}	
	public void setFonts(List<JsonFont> fonts) {this.fonts = fonts;}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}