package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;

public class JsonDbMetaTableFactory
{
	public static JsonPostgresMetaTable build(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaTableFactory.build(rs.getString("TABLE_NAME"));
	}
	
	public static JsonPostgresMetaTable build(String name) throws IOException
	{
		JsonPostgresMetaTable json = new JsonPostgresMetaTable();
		json.setName(name);
		return json;
	}
	
	public static List<String> toCodes(List<JsonPostgresMetaTable> list)
	{
		 List<String> result = new ArrayList<>();
		 for(JsonPostgresMetaTable t : list) {result.add(t.getName());}
		 return result;
	}
}