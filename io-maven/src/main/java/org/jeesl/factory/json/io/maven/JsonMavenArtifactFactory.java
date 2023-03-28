package org.jeesl.factory.json.io.maven;

import org.jeesl.model.json.io.maven.JsonMavenArtifact;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlArtifact;

public class JsonMavenArtifactFactory
{
	public static JsonMavenArtifact build(JsonMavenFerstlArtifact ferstl)
	{
		JsonMavenArtifact json = new JsonMavenArtifact();
		
		json.setId(ferstl.getNumericId());
		json.setGroupId(ferstl.getGroupId());
		json.setArtifactId(ferstl.getArtifactId());
		json.setVersion(ferstl.getVersion());
		
		return json;
	}
}