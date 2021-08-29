package org.jeesl.factory.sql.system.db;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.factory.xml.system.io.db.XmlQueryFactory;
import org.jeesl.model.xml.system.io.db.Query;

public class SqlDbPgStatFactory
{
	public static String connections(String userName)
	{
		List<String> fileds = new ArrayList<String>();
		fileds.add("xact_start");
		fileds.add("query_start");
		fileds.add("state_change");
		fileds.add("state");
		fileds.add("query");

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(StringUtils.join(fileds,","));
		sb.append(" FROM pg_stat_activity");
		sb.append(" WHERE usename='").append(userName).append("'");
		
		return sb.toString();
	}
	public static Query connections(int number, Object[] array)
	{
		Timestamp tsTransaction = null;	if(array[0]!=null){tsTransaction = (Timestamp)array[0];}
		Timestamp tsQuery = null;		if(array[1]!=null){tsQuery = (Timestamp)array[1];}
		Timestamp tsState = null;		if(array[2]!=null){tsState = (Timestamp)array[2];}
	
		String state = null;			if(array[3]!=null){state = (String)array[3];}
		String query = null;			if(array[4]!=null){query = (String)array[4];}
	
		return XmlQueryFactory.build(tsTransaction,tsQuery,tsState,state,query);
	}
	
	public static String statements(String userName)
	{
		List<String> fileds = new ArrayList<String>();
		fileds.add("pd.datname");
		fileds.add("pss.query AS SQLQuery");
		fileds.add("pss.rows AS TotalRowCount");
		fileds.add("calls");
		fileds.add("pss.total_time");
		fileds.add("pss.mean_time");

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ").append(StringUtils.join(fileds,","));
		sb.append(" FROM pg_stat_statements AS pss");
		sb.append(" INNER JOIN pg_database AS pd");
		sb.append("            ON pss.dbid=pd.oid");
		sb.append(" ORDER BY pss.mean_time DESC ");
		sb.append(" LIMIT 200;");
		
		return sb.toString();
	}
}