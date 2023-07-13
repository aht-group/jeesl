package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;

public class JsonPostgresMetaConstraintFactory
{
	public static JsonPostgresMetaConstraint build(ResultSet rs) throws IOException, SQLException
	{
		return JsonPostgresMetaConstraintFactory.build(rs.getString("fk_name"));
	}
	
	public static JsonPostgresMetaConstraint build(String code) throws IOException
	{
		JsonPostgresMetaConstraint json = new JsonPostgresMetaConstraint();
		json.setCode(code);
		return json;
	}
}