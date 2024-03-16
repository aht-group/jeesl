package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.factory.json.system.status.JsonTypeFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;

public class JsonDbMetaColumnFactory
{
	public static JsonPostgresMetaColumn build(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaColumnFactory.build(rs.getString("COLUMN_NAME"),rs.getString("TYPE_NAME"));
	}
	
	public static JsonPostgresMetaColumn build(String code, String type) throws IOException
	{
		JsonPostgresMetaColumn json = new JsonPostgresMetaColumn();
		json.setCode(code);
		json.setType(JsonTypeFactory.build(type));
		return json;
	}
	
	public static JsonPostgresMetaColumn build(JsonPostgresMetaTable table, String code) throws IOException
	{
		JsonPostgresMetaColumn json = new JsonPostgresMetaColumn();
		json.setTable(table);
		json.setCode(code);
		return json;
	}
}