package org.jeesl.controller.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.exlp.util.io.StringUtil;
import org.openfuxml.exception.OfxAuthoringException;
import org.openfuxml.renderer.text.OfxTextRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NativeQueryDebugger
{
	final static Logger logger = LoggerFactory.getLogger(NativeQueryDebugger.class);
		
	public static void debug(Connection connection, String sql, boolean showSql, boolean executeQuery)
	{
		if(showSql)
		{
			System.out.println(StringUtil.stars());
			System.out.println(sql);
		}
		
		if(executeQuery)
		{
			System.out.println(StringUtil.stars());
			   
		    try 
		    {
		    	Statement statement = connection.createStatement();
		    	ResultSet rs = statement.executeQuery(sql);
		    	OfxTextRenderer.table(rs, System.out);
		    	DbUtils.closeQuietly(rs);
		    }
		    catch (SQLException e) {e.printStackTrace();}
		    catch (OfxAuthoringException e) {e.printStackTrace();}
		    catch (IOException e) {e.printStackTrace();}
			finally{DbUtils.closeQuietly(connection);}
		}
	}
}