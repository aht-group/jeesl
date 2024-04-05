package org.jeesl.factory.json.io.db.pg;

import org.jeesl.model.json.io.db.pg.JsonPostgres;

public class JsonPostgresFactory 
{
	public static JsonPostgres build()
	{
		return new JsonPostgres();
	}
}