package org.jeesl.util.query.sql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlRevisionQueries
{
	final static Logger logger = LoggerFactory.getLogger(SqlRevisionQueries.class);

	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public static String revisionsIn(String entityTable, String revisionTable, Date from, Date to, List<Integer> types)
	{	
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT e.id as id,\n");
		sb.append("        r.id as rev,\n");
		sb.append("        e.revtype as type,\n");
		sb.append("        r.userid as user\n");
		sb.append("FROM ").append(entityTable).append(" e\n");
		sb.append("JOIN ").append(revisionTable).append(" r ON e.rev = r.id\n");
		sb.append("WHERE TRUE\n");
		sb.append("    AND e.revtype IN (").append(StringUtils.join(types,",")).append(")\n");
		if(from!=null){sb.append("    AND r.auditrecord >= '").append(df.format(from)).append("'\n");}
		if(to!=null){sb.append  ("    AND r.auditrecord <= '").append(df.format(to)).append("'");}

		return sb.toString();
	}
	
	public static String idsLive(String entityTable)
	{	
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id\n");
		sb.append(" FROM ").append(entityTable).append("\n");
		return sb.toString();
	}
	
	public static String idsRevision(String entityTable)
	{	
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id\n");
		sb.append("FROM ").append(entityTable).append("\n");
		sb.append("GROUP BY id ");
		return sb.toString();
	}
	
	public static List<Integer> typesCreateRemove()
	{
		List<Integer> result = new ArrayList<Integer>();
		result.add(0);
		result.add(2);
		return result;
	}
}