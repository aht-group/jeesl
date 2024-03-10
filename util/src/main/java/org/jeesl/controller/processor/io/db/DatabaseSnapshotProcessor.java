package org.jeesl.controller.processor.io.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.util.io.JsonUtil;
import org.jeesl.controller.handler.io.log.LoggedExit;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaColumnFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaConstraintFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaTableFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaConstraint;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSnapshotProcessor
{
	final static Logger logger = LoggerFactory.getLogger(DatabaseSnapshotProcessor.class);

	private final Connection connection;
	
	private final Set<String> filteredTables;
	
	public DatabaseSnapshotProcessor filter(String table) {filteredTables.add(table); return this;}
	
	public static DatabaseSnapshotProcessor instance(Connection connection) {return new DatabaseSnapshotProcessor(connection);}
	public DatabaseSnapshotProcessor(Connection connection)
	{
		this.connection=connection;
		filteredTables = new HashSet<>();
	}
	
	public JsonPostgresMetaSnapshot snapshot() throws SQLException, IOException
	{		
		JsonPostgresMetaSnapshot jSnapshot = new JsonPostgresMetaSnapshot();
		jSnapshot.setRecord(LocalDateTime.now());
		jSnapshot.setTables(new ArrayList<>());
		
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsTable = meta.getTables(null, null, null, new String[]{"TABLE"});

		while(rsTable.next())
		{
			JsonPostgresMetaTable table = JsonDbMetaTableFactory.build(rsTable);
		
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
				
				table.setUniqueKeys(new ArrayList<>());
				Map<String,JsonPostgresMetaConstraint> mapUk = new HashMap<>();
				ResultSet rsUk = meta.getIndexInfo(null,null, table.getCode(), true, false);
				logger.trace("Uniques of "+table.getCode());
				while(rsUk.next())
				{
					for(int i=1;i<=rsUk.getMetaData().getColumnCount();i++){logger.trace(i+" "+rsUk.getMetaData().getColumnName(i)+": "+rsUk.getString(i));}
					JsonDbMetaConstraintFactory.addUk(rsUk,mapUk);
				}
				if(ObjectUtils.isNotEmpty(mapUk.values()))
				{
					for(JsonPostgresMetaConstraint c : mapUk.values())
					{
						if(!mapPk.containsKey(c.getCode())) {table.getUniqueKeys().add(c);}
					}
				}
				
				if(table.getCode().equals("xxIoReportSheet".toLowerCase()))
				{
					JsonUtil.info(table);
					LoggedExit.exit(true);
				}
				
				
				jSnapshot.getTables().add(table);
			}
		}
		return jSnapshot;
	}
}