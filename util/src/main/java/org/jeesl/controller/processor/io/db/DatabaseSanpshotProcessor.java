package org.jeesl.controller.processor.io.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.jeesl.factory.json.io.db.meta.JsonDbMetaColumnFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaConstraintFactory;
import org.jeesl.factory.json.io.db.meta.JsonDbMetaTableFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSanpshotProcessor
{
	final static Logger logger = LoggerFactory.getLogger(DatabaseSanpshotProcessor.class);

	public static JsonPostgresMetaSnapshot snapshot(Connection connection) throws SQLException, IOException
	{		
		JsonPostgresMetaSnapshot jSnapshot = new JsonPostgresMetaSnapshot();
		jSnapshot.setRecord(LocalDateTime.now());
		jSnapshot.setTables(new ArrayList<>());
		
		DatabaseMetaData meta = connection.getMetaData();
		ResultSet rsTable = meta.getTables(null, null, null, new String[]{"TABLE"});

		while(rsTable.next())
		{
			JsonPostgresMetaTable table = JsonDbMetaTableFactory.build(rsTable);
			
			table.setColumns(new ArrayList<>());
			logger.trace("Columns of "+table.getCode());
			ResultSet rsColumn = meta.getColumns(null,null, table.getCode(), null);
			int rsColumnsNumber = rsColumn.getMetaData().getColumnCount();
			while(rsColumn.next())
			{
				table.getColumns().add(JsonDbMetaColumnFactory.build(rsColumn));
				for(int i=1;i<=rsColumnsNumber;i++)
				{
					logger.trace(i+" "+rsColumn.getMetaData().getColumnName(i)+": "+rsColumn.getString(i));
				}
			}
			
			table.setForeignKeys(new ArrayList<>());
			ResultSet rsFk = meta.getImportedKeys(null, null, table.getCode());
			int columnsNumber = rsFk.getMetaData().getColumnCount();
			logger.trace("Foreign Keys of "+table.getCode());
			while(rsFk.next())
			{
				table.getForeignKeys().add(JsonDbMetaConstraintFactory.build(rsFk));
				for(int i=1;i<=columnsNumber;i++)
				{
					logger.trace(i+" "+rsFk.getMetaData().getColumnName(i)+": "+rsFk.getString(i));
				}
			}
			
			jSnapshot.getTables().add(table);
		}
		return jSnapshot;
	}
}