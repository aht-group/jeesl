package org.jeesl.factory.json.io.ssi.domain.svn;

import org.jeesl.model.json.io.ssi.svn.JsonSvnRevision;

public class JsonSvnRevisionFactory
{
	public static JsonSvnRevision build() {return new JsonSvnRevision();}
	
	public static JsonSvnRevision build(long version)
	{
		JsonSvnRevision json = JsonSvnRevisionFactory.build();
		json.setVersion(version);
		return json;
	}
}