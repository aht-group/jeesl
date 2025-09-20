package org.jeesl.factory.json.io.ssi.domain.svn;

import org.jeesl.model.json.io.ssi.svn.JsonSvnRepository;

public class JsonSvnRepositoryFactory
{
	public static JsonSvnRepository build() {return new JsonSvnRepository();}
	
	public static JsonSvnRepository build(String code)
	{
		JsonSvnRepository json = JsonSvnRepositoryFactory.build();
		json.setCode(code);
		return json;
	}
}