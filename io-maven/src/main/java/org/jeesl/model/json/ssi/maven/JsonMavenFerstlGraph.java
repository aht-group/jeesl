package org.jeesl.model.json.ssi.maven;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

//@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="graph")
public class JsonMavenFerstlGraph implements Serializable
{
	public static final long serialVersionUID=1;

	@JsonProperty("graphName")
	private String graphName;
	public String getGraphName() {return graphName;}
	public void setGraphName(String graphName) {this.graphName = graphName;}

	@JsonProperty("artifacts")
	private List<JsonMavenFerstlArtifact> artifacts;
	public List<JsonMavenFerstlArtifact> getArtifacts() {return artifacts;}
	public void setArtifacts(List<JsonMavenFerstlArtifact> artifacts) {this.artifacts = artifacts;}
	
	@JsonProperty("dependencies")
	private List<JsonMavenFerstlDependency> dependencies;
	public List<JsonMavenFerstlDependency> getDependencies() {return dependencies;}
	public void setDependencies(List<JsonMavenFerstlDependency> dependencies) {this.dependencies = dependencies;}
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();

		return sb.toString();
	}
}