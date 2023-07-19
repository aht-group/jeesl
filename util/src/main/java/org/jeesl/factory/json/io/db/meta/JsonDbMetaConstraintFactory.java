package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;

public class JsonDbMetaConstraintFactory
{
	public static JsonPostgresMetaConstraint buildPk(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("pk_name"),rs.getString("column_name"),null,null);
	}
	public static JsonPostgresMetaConstraint buildUk(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("index_name"),rs.getString("column_name"),null,null);
	}
	public static JsonPostgresMetaConstraint buildFk(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("fk_name"),rs.getString("fkcolumn_name"),rs.getString("pktable_name"),rs.getString("pkcolumn_name"));
	}
	
	public static JsonPostgresMetaConstraint build(String code, String localColumn, String remoteTable, String remoteColumn) throws IOException
	{
		JsonPostgresMetaConstraint json = new JsonPostgresMetaConstraint();
		json.setCode(code);
		json.setLocalColumn(localColumn);
		json.setRemoteTable(remoteTable);
		json.setRemoteColumn(remoteColumn);
		return json;
	}
}