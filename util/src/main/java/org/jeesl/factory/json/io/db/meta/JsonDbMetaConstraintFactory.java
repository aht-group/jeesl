package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;

public class JsonDbMetaConstraintFactory
{
	public static JsonPostgresMetaConstraint build(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("fk_name"),rs.getString("fkcolumn_name"),rs.getString("pktable_name"),rs.getString("pkcolumn_name"));
	}
	
	public static JsonPostgresMetaConstraint build(String code, String localColumn, String referencedTable, String referencedColumn) throws IOException
	{
		JsonPostgresMetaConstraint json = new JsonPostgresMetaConstraint();
		json.setCode(code);
		json.setLocalColumn(localColumn);
		json.setReferencedTable(referencedTable);
		json.setReferencedColumn(referencedColumn);
		
		return json;
	}
}