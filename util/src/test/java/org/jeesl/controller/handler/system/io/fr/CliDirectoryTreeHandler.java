package org.jeesl.controller.handler.system.io.fr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import org.jeesl.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliDirectoryTreeHandler
{
	final static Logger logger = LoggerFactory.getLogger(CliDirectoryTreeHandler.class);
	
	private DirectoryTreeHandler dth;
	
	public CliDirectoryTreeHandler()
	{
		File root = new File("./target");
		logger.info(root.getAbsolutePath());
		dth = new DirectoryTreeHandler(root.toPath(),2,3);
	}
	
	private void test() throws IOException
	{
		String x = UUID.randomUUID().toString();
		logger.info("Start: "+x);
		Path p = dth.create(x);
		logger.info(Path.class.getSimpleName()+" "+p.toString());
	}
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		CliDirectoryTreeHandler cli = new CliDirectoryTreeHandler();
		cli.test();
	}
}