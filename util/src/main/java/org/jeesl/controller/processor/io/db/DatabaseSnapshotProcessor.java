package org.jeesl.controller.processor.io.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.controller.handler.io.log.LoggedExit;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaColumnFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaConstraintFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaTableFactory;
import org.jeesl.factory.json.io.db.meta.JsonPostgresTablespaceFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSnapshotProcessor
{
	final static Logger logger = LoggerFactory.getLogger(DatabaseSnapshotProcessor.class);

	private final Connection connection;
	
	private final Map<String,String> mapTbsIndex,mapTbsTable;
	private final Set<String> filteredTables;
	
	private Statement stmt;
    private ResultSet rs;
	
	public DatabaseSnapshotProcessor filter(String table) {filteredTables.add(table); return this;}
	
	public static DatabaseSnapshotProcessor instance(Connection connection) {return new DatabaseSnapshotProcessor(connection);}
	public DatabaseSnapshotProcessor(Connection connection)
	{
		this.connection=connection;
		filteredTables = new HashSet<>();
		mapTbsIndex = new HashMap<>();
		mapTbsTable = new HashMap<>();
		
		stmt = null;
		rs = null;
	}
	
	public JsonPostgresMetaSnapshot snapshot() throws SQLException, IOException
	{		
		JsonPostgresMetaSnapshot jSnapshot = new JsonPostgresMetaSnapshot();
		jSnapshot.setRecord(LocalDateTime.now());
		jSnapshot.setTables(new ArrayList<>());
		
		this.tablespaces();
		
		DatabaseMetaData meta = connection.getMetaData();
		
	
		ResultSet rsTable = meta.getTables(null, null, null, new String[]{"TABLE"});
		while(rsTable.next())
		{
			for(int i=1;i<=rsTable.getMetaData().getColumnCount();i++) {logger.trace(i+" "+rsTable.getMetaData().getColumnName(i)+": "+rsTable.getString(i));}
			JsonPostgresMetaTable table = JsonDbMetaTableFactory.build(rsTable);
			if(mapTbsTable.containsKey(table.getCode())) {table.setTablespace(JsonPostgresTablespaceFactory.build(mapTbsTable.get(table.getCode())));}
			Optional.ofNullable(mapTbsTable.get(table.getCode())).ifPresent(v -> {table.setTablespace(JsonPostgresTablespaceFactory.build(v));});
			
			boolean proceed = true;
			if(ObjectUtils.isNotEmpty(filteredTables)) {proceed = filteredTables.contains(table.getCode());}
			
			if(proceed)
			{
				table.setColumns(new ArrayList<>());
				table.setPrimaryKeys(new ArrayList<>());
				table.setForeignKeys(new ArrayList<>());
				
				logger.trace("Columns of "+table.getCode());
				ResultSet rsColumn = meta.getColumns(null,null, table.getCode(), null);
				while(rsColumn.next())
				{
					for(int i=1;i<=rsColumn.getMetaData().getColumnCount();i++) {logger.trace(i+" "+rsColumn.getMetaData().getColumnName(i)+": "+rsColumn.getString(i));}
					table.getColumns().add(JsonDbMetaColumnFactory.build(rsColumn));
				}
				
				Map<String,JsonPostgresMetaConstraint> mapPk = new HashMap<>();
				ResultSet rsPk = meta.getPrimaryKeys(null, null, table.getCode());
				logger.trace("Primary Keys of "+table.getCode());
				while(rsPk.next())
				{
					for(int i=1;i<=rsPk.getMetaData().getColumnCount();i++) {logger.trace(i+" "+rsPk.getMetaData().getColumnName(i)+": "+rsPk.getString(i));}
					JsonPostgresMetaConstraint c = JsonDbMetaConstraintFactory.buildPk(rsPk);
					mapPk.put(c.getCode(),c);
					table.getPrimaryKeys().add(c);
				}
				
				ResultSet rsFk = meta.getImportedKeys(null,null, table.getCode());
				logger.trace("Foreign Keys of "+table.getCode());
				while(rsFk.next())
				{
					for(int i=1;i<=rsFk.getMetaData().getColumnCount();i++){logger.trace(i+" "+rsFk.getMetaData().getColumnName(i)+": "+rsFk.getString(i));}
					table.getForeignKeys().add(JsonDbMetaConstraintFactory.buildFk(rsFk));
				}
				
				Map<String,JsonPostgresMetaConstraint> mapUk = new HashMap<>();
				Map<String,JsonPostgresMetaConstraint> mapIdx = new HashMap<>();
				ResultSet rsIdx = meta.getIndexInfo(null,null, table.getCode(), false, false);
				logger.trace("Uniques of "+table.getCode());
				while(rsIdx.next())
				{
					for(int i=1;i<=rsIdx.getMetaData().getColumnCount();i++){logger.trace(i+" "+rsIdx.getMetaData().getColumnName(i)+": "+rsIdx.getString(i));}
					boolean unique = !rsIdx.getBoolean("NON_UNIQUE");
					if(unique) {JsonDbMetaConstraintFactory.addUk(rsIdx,mapUk);}
					else {JsonDbMetaConstraintFactory.addUk(rsIdx,mapIdx);}
				}
				
				table.setUniqueKeys(new ArrayList<>());
				table.setIndexes(new ArrayList<>());
				if(ObjectUtils.isNotEmpty(mapUk.values()))
				{
					for(JsonPostgresMetaConstraint c : mapUk.values())
					{
						if(mapTbsIndex.containsKey(c.getCode())) {c.setTablespace(JsonPostgresTablespaceFactory.build(mapTbsIndex.get(c.getCode())));}
						boolean isPk = mapPk.containsKey(c.getCode());
						if(!isPk) {table.getUniqueKeys().add(c);}
					}
				}
				if(ObjectUtils.isNotEmpty(mapIdx.values()))
				{
					for(JsonPostgresMetaConstraint c : mapIdx.values())
					{
						if(mapTbsIndex.containsKey(c.getCode())) {c.setTablespace(JsonPostgresTablespaceFactory.build(mapTbsIndex.get(c.getCode())));}
						boolean isPk = mapPk.containsKey(c.getCode());
						boolean isUk = mapUk.containsKey(c.getCode());
						if(!isPk && !isUk) {table.getIndexes().add(c);}
					}
				}
				
				if(table.getCode().equals("xxxxIoSsiData".toLowerCase())) {JsonUtil.info(table);LoggedExit.exit(true);}
				jSnapshot.getTables().add(table);
			}
		}
		return jSnapshot;
	}
	
	private void tablespaces()
	{
		try
		{
			stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM pg_indexes WHERE tablespace IS NOT NULL;");

            while (rs.next())
            {
            	String idxName = rs.getString("indexname");
            	String tbs = rs.getString("tablespace");
            	mapTbsIndex.put(idxName,tbs);
            }
        }
		catch (SQLException e) {e.printStackTrace();}
		finally {DbUtils.closeQuietly(rs);DbUtils.closeQuietly(stmt);}
		
		try
		{
			stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM pg_tables WHERE tablespace IS NOT NULL;");

            while (rs.next())
            {
            	String table = rs.getString("tablename");
            	String space = rs.getString("tablespace");
            	mapTbsTable.put(table,space);
            }
        }
		catch (SQLException e) {e.printStackTrace();}
		finally {DbUtils.closeQuietly(rs);DbUtils.closeQuietly(stmt);}
	}
}