package org.jeesl.client.test.controller.processor.io.db;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.dbutils.DbUtils;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.controller.processor.io.db.DatabaseSanpshotProcessor;
import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.sf.exlp.util.io.JsonUtil;

public class CliDbMetaSnapshot
{
	final static Logger logger = LoggerFactory.getLogger(CliDbMetaSnapshot.class);
	
	private final Configuration config;
	
	public CliDbMetaSnapshot(Configuration config) throws NamingException
	{
		this.config=config;
	}
	
	private void metaSnapshot(String systemCode) throws SQLException, JsonGenerationException, JsonMappingException, IOException
	{
		JsonPostgresMetaSnapshot snapshot = new JsonPostgresMetaSnapshot();
		
		Connection connection = null;
		try
		{
			connection = JeeslBootstrap.createMeisDatasource(config,systemCode).getConnection();
			snapshot = DatabaseSanpshotProcessor.snapshot(connection);
			snapshot.setSystem(JsonSsiSystemFactory.build(systemCode));
		}
		
	    catch (SQLException | IOException e) {e.printStackTrace();}
		finally{DbUtils.closeQuietly(connection);}
		
		JsonUtil.info(snapshot);
		Path path = Paths.get(config.getString("dir.onedrive"),"dev/jeesl/io/db/meta/"+systemCode+".json");
		JsonUtil.write(snapshot, path.toFile());
	}
	
	public static void main(String args[]) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
//		MeisBootstrap.createMeisDatasource(config);
		
		CliDbMetaSnapshot cli = new CliDbMetaSnapshot(config);
		cli.metaSnapshot("ofx");
	}
}