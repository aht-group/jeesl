package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;

public class JsonDbMetaColumnFactory
{
	public static JsonPostgresMetaColumn build(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaColumnFactory.build(rs.getString("COLUMN_NAME"));
	}
	
	public static JsonPostgresMetaColumn build(String code) throws IOException
	{
		JsonPostgresMetaColumn json = new JsonPostgresMetaColumn();
		json.setCode(code);
		return json;
	}
}