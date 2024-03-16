package org.jeesl.factory.json.io.db.meta;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresTablespace;

public class JsonPostgresTablespaceFactory
{	
	public static JsonPostgresTablespace build(String code)
	{
		JsonPostgresTablespace json = new JsonPostgresTablespace();
		json.setCode(code);
		return json;
	}
}