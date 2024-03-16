package org.jeesl.controller.handler.system.io.fr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import org.jeesl.test.JeeslBootstrap;
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
	
	public void test() throws IOException
	{
		String x = UUID.randomUUID().toString();
		logger.info("Start: "+x);
		Path p = dth.create(x);
		logger.info(Path.class.getSimpleName()+" "+p.toString());
	}
	
	private void padding()
	{
		String x = "abc";
		Path p = dth.build(x);
		logger.info(Path.class.getSimpleName()+" "+p.toString());
	}
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		CliDirectoryTreeHandler cli = new CliDirectoryTreeHandler();
//		cli.test();
		cli.padding();
	}
}