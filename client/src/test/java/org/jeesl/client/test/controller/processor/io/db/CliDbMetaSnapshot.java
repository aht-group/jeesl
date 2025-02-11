package org.jeesl.client.test.controller.processor.io.db;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.commons.dbutils.DbUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.JsonUtil;
import org.jeesl.client.JeeslBootstrap;
import org.jeesl.controller.processor.io.db.DatabaseSnapshotProcessor;
import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.db.pg.meta.JsonPostgresMetaSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
			connection = JeeslBootstrap.buildDatasource(config,systemCode).getConnection();
			snapshot = DatabaseSnapshotProcessor.instance(connection).snapshot();
			snapshot.setSystem(JsonSsiSystemFactory.build(systemCode));
		}
		
	    catch (SQLException | IOException e) {e.printStackTrace();}
		finally {DbUtils.closeQuietly(connection);}
		
		JsonUtil.trace(snapshot);
		Path path = Paths.get(config.getString("dir.onedrive"),"dev/jeesl/io/db/meta/"+systemCode+".json");
		JsonUtil.write(snapshot, path.toFile());
	}
	
	public static void main(String args[]) throws Exception
	{
		Configuration config = JeeslBootstrap.wrap();
//		MeisBootstrap.createMeisDatasource(config);
		
		CliDbMetaSnapshot cli = new CliDbMetaSnapshot(config);
		cli.metaSnapshot("jeesl");
	}
}