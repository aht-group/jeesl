package org.jeesl.factory.json.io.db.meta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSchema;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;

public class JsonDbMetaSchemaFactory
{
	public static JsonPostgresMetaSchema build(String code)
	{
		JsonPostgresMetaSchema json = new JsonPostgresMetaSchema();
		json.setCode(code);
		return json;
	}
	
	public static List<String> toCodes(List<JsonPostgresMetaSchema> schemas)
	{
		 List<String> result = new ArrayList<>();
		 if(Objects.nonNull(schemas))
		 {
			 for(JsonPostgresMetaSchema s : schemas) {result.add(s.getCode());}
		 }
		 return result;
	}
}