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
			logger.info("Columns of "+table.getCode());
			ResultSet rsColumn = meta.getColumns(null,null, table.getCode(), null);
			int rsColumnsNumber = rsColumn.getMetaData().getColumnCount();
			while(rsColumn.next())
			{
				table.getColumns().add(JsonDbMetaColumnFactory.build(rsColumn));
				for(int i=1;i<=rsColumnsNumber;i++)
				{
					logger.info(i+" "+rsColumn.getMetaData().getColumnName(i)+": "+rsColumn.getString(i));
				}
			    String columnName = rsColumn.getString("COLUMN_NAME");
			    String datatype = rsColumn.getString("DATA_TYPE");
			    String columnsize = rsColumn.getString("COLUMN_SIZE");
			    String decimaldigits = rsColumn.getString("DECIMAL_DIGITS");
			    String isNullable = rsColumn.getString("IS_NULLABLE");
			    String is_autoIncrment = rsColumn.getString("IS_AUTOINCREMENT");
			    //Printing results
			    System.out.println(columnName + "---" + datatype + "---" + columnsize + "---" + decimaldigits + "---" + isNullable + "---" + is_autoIncrment);
			}
			
			table.setForeignKeys(new ArrayList<>());
			ResultSet rsFk = meta.getImportedKeys(null, null, table.getCode());
			int columnsNumber = rsFk.getMetaData().getColumnCount();
			logger.info("Foreign Keys of "+table.getCode());
			while(rsFk.next())
			{
				table.getForeignKeys().add(JsonDbMetaConstraintFactory.build(rsFk));
				for(int i=1;i<=columnsNumber;i++)
				{
					logger.info(i+" "+rsFk.getMetaData().getColumnName(i)+": "+rsFk.getString(i));
				}
//				    System.out.println(rsFk.getString("PKTABLE_NAME") + "." + rsFk.getString("PKCOLUMN_NAME") + "===" + rsFk.getString("FKTABLE_NAME") + "." + rsFk.getString("FKCOLUMN_NAME"));
			}
			
			jSnapshot.getTables().add(table);
		}
		return jSnapshot;
	}
}