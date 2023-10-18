package org.jeesl.factory.txt.io.mail;

import org.jeesl.JeeslBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTxtMailEmailFactory
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtMailEmailFactory.class);

	public TestTxtMailEmailFactory()
	{
		
	}
	
	@Test
	public void outlook()
	{
		Assertions.assertEquals("a@b.c", TxtMailEmailFactory.parseEmail("a b c <a@b.c>"));
	}
	
	@Test
	public void correct()
	{
		Assertions.assertEquals("a@b.c", TxtMailEmailFactory.parseEmail("a@b.c"));
	}

	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		TestTxtMailEmailFactory cli = new TestTxtMailEmailFactory();
		
		cli.outlook();
	}
}