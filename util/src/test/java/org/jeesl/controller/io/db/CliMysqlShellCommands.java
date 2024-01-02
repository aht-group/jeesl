package org.jeesl.controller.io.db;

import org.jeesl.JeeslBootstrap;
import org.jeesl.controller.io.db.shell.mysql.MySqlShellCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliMysqlShellCommands
{
	final static Logger logger = LoggerFactory.getLogger(CliMysqlShellCommands.class);
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		logger.info(MySqlShellCommands.dropDatabase("root","testDb"));
		logger.info(MySqlShellCommands.createDatabase("root","testDb"));
		logger.info(MySqlShellCommands.grantDatabase("root","testDb","testUser","testPwd"));
	}
}