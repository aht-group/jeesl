package org.jeesl.factory.json.io.db.meta;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaColumn;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;

public class JsonDbMetaConstraintFactory
{
	public static JsonPostgresMetaConstraint buildPk(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("pk_name"),rs.getString("table_name"),rs.getString("column_name"),null,null);
	}
	public static JsonPostgresMetaConstraint buildFk(ResultSet rs) throws IOException, SQLException
	{
		return JsonDbMetaConstraintFactory.build(rs.getString("fk_name"),rs.getString("fktable_name"),rs.getString("fkcolumn_name"),rs.getString("pktable_name"),rs.getString("pkcolumn_name"));
	}
	public static void addUk(ResultSet rs, Map<String,JsonPostgresMetaConstraint> map) throws IOException, SQLException
	{
		String code = rs.getString("index_name");
		if(!map.containsKey(code))
		{
			JsonPostgresMetaConstraint json = new JsonPostgresMetaConstraint();
			json.setCode(code);
			json.setColumns(new ArrayList<>());
			map.put(code,json);
		}
		
		JsonPostgresMetaColumn c = new JsonPostgresMetaColumn();
		c.setPosition(rs.getInt("ORDINAL_POSITION"));
		c.setCode(rs.getString("COLUMN_NAME"));
		
		map.get(code).getColumns().add(c);
	}
	
	public static JsonPostgresMetaConstraint build(String code,
													String localTable, String localColumn,
													String remoteTable, String remoteColumn) throws IOException
	{
		JsonPostgresMetaConstraint json = new JsonPostgresMetaConstraint();
		json.setCode(code);
		json.setLocal(JsonDbMetaColumnFactory.build(JsonDbMetaTableFactory.build(null,localTable),localColumn));
		json.setLocalColumn(localColumn);
		json.setRemoteTable(remoteTable);
		json.setRemoteColumn(remoteColumn);
		return json;
	}
}