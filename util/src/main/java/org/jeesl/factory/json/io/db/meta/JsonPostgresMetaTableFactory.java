package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;

public class JsonPostgresMetaTableFactory
{
	public static JsonPostgresMetaTable build(ResultSet rs) throws IOException, SQLException
	{
		return JsonPostgresMetaTableFactory.build(rs.getString("TABLE_NAME"));
	}
	
	public static JsonPostgresMetaTable build(String name) throws IOException
	{
		JsonPostgresMetaTable json = new JsonPostgresMetaTable();
		json.setName(name);
		return json;
	}
}