package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSchema;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;

public class JsonDbMetaTableFactory
{
	public static JsonPostgresMetaTable build(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaTableFactory.build(rs.getString("TABLE_SCHEM"),rs.getString("TABLE_NAME"));
	}
	
	public static JsonPostgresMetaTable build(String scheme, String code) throws IOException
	{
		JsonPostgresMetaTable json = new JsonPostgresMetaTable();
		json.setScheme(scheme);
		json.setCode(code);
		return json;
	}
	
	public static List<String> toCodes(JsonPostgresMetaSnapshot snapshot)
	{
		 List<String> result = new ArrayList<>();
		 if(Objects.nonNull(snapshot.getTables()))
		 {
			 for(JsonPostgresMetaTable t : snapshot.getTables()) {result.add(t.getCode());}
		 }
		 return result;
	}
	
	public static List<JsonPostgresMetaSchema> toSchemas(List<JsonPostgresMetaTable> tables)
	{
		List<JsonPostgresMetaSchema> list = new ArrayList<>();
		if(Objects.nonNull(list))
		{
			Set<String> set = new HashSet<>();
			for(JsonPostgresMetaTable t : tables)
			{
				set.add(t.getScheme());
			}
			for(String s : set)
			{
				list.add(JsonDbMetaSchemaFactory.build(s));
			}
		}
		return list;
	}
}