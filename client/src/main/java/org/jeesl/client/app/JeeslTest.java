package org.jeesl.client.app;

import java.io.File;

import org.jeesl.client.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.doc.loc.LoccerTask;

public class JeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(JeeslTest.class);

	public void loc()
	{
		File f = new File(System.getProperty("user.dir"));
		logger.info(f.getAbsolutePath());
		logger.info(f.getParentFile().getAbsolutePath());
		File x = new File(f.getParentFile().getParentFile().getAbsoluteFile(),"ofx");
		logger.info(x.getAbsolutePath());
		new LoccerTask(x.getAbsolutePath());
	}
	
	public static void main(String args[])
	{
		JeeslBootstrap.init();
		System.out.println("Hello World");
		
		JeeslTest cli = new JeeslTest();
		cli.loc();
	}
}