package net.sf.ahtutils.controller.util;

import org.jeesl.JeeslUtilTestBootstrap;
import org.jeesl.factory.txt.system.security.user.TxtPasswordGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtilsPasswordGenerator
{
	final static Logger logger = LoggerFactory.getLogger(TestUtilsPasswordGenerator.class);
		
	@Test
	public void pwdSize()
	{
		for(int i=5;i<15;i++)
		{
			String pwd = TxtPasswordGenerator.random(i);
			Assert.assertEquals(i, pwd.length());
		}
	}
	
	public void direct()
	{
		logger.debug(TxtPasswordGenerator.random());
	}
	
	public static void main (String[] args) throws Exception
	{
		JeeslUtilTestBootstrap.init();
		logger.debug("Test");
		TestUtilsPasswordGenerator test = new TestUtilsPasswordGenerator();
		test.direct();
	}
}