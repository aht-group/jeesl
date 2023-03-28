package org.jeesl.factory.json.io.maven;

import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlArtifact;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlDependency;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlGraph;

public class JsonMavenGraphFactory
{
	public static JsonMavenGraph build() {return  new JsonMavenGraph();}
	
	public static JsonMavenGraph build(JsonMavenFerstlGraph ferstl)
	{
		JsonMavenGraph json = JsonMavenGraphFactory.build();
		json.setCode(ferstl.getGraphName());
		
		if(ObjectUtils.isNotEmpty(ferstl.getArtifacts()))
		{
			json.setArtifacts(new ArrayList<>());
			for(JsonMavenFerstlArtifact artifact : ferstl.getArtifacts())
			{
				json.getArtifacts().add(JsonMavenArtifactFactory.build(artifact));
			}
		}
		
		if(ObjectUtils.isNotEmpty(ferstl.getDependencies()))
		{
			json.setDependencies(new ArrayList<>());
			for(JsonMavenFerstlDependency dependency : ferstl.getDependencies())
			{
				json.getDependencies().add(JsonMavenDependencyFactory.build(dependency));
			}
		}
		
		return json;
	}
}