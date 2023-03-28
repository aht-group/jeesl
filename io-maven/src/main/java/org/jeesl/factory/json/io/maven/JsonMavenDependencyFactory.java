package org.jeesl.factory.json.io.maven;

import org.jeesl.model.json.io.maven.JsonMavenDependency;
import org.jeesl.model.json.ssi.maven.JsonMavenFerstlDependency;

public class JsonMavenDependencyFactory
{
	public static JsonMavenDependency build(JsonMavenFerstlDependency ferstl)
	{
		JsonMavenDependency json = new JsonMavenDependency();

		json.setFrom(ferstl.getNumericFrom());
		json.setTo(ferstl.getNumericTo());
		
		return json;
	}
}