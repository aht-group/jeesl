package org.jeesl.factory.json.io.maven;

import java.util.ArrayList;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.factory.json.system.status.JsonScopeFactory;
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
		
		if(ObjectUtils.isNotEmpty(ferstl.getScopes()))
		{
			json.setScopes(new ArrayList<>());
			for(String scope : ferstl.getScopes())
			{
				json.getScopes().add(JsonScopeFactory.build(scope));
			}
		}
		return json;
	}
}