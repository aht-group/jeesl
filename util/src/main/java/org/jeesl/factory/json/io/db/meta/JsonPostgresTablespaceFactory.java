package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresTablespace;

public class JsonPostgresTablespaceFactory
{	
	public static JsonPostgresTablespace build(String code) throws IOException
	{
		JsonPostgresTablespace json = new JsonPostgresTablespace();
		json.setCode(code);
		return json;
	}
}