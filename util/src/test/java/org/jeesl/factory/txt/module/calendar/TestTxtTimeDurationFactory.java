package org.jeesl.factory.txt.module.calendar;

import org.jeesl.JeeslBootstrap;
import org.jeesl.factory.txt.io.mail.TxtMailEmailFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTxtTimeDurationFactory
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtTimeDurationFactory.class);

	public TestTxtTimeDurationFactory()
	{
		
	}
	
	@Test
	public void compare()
	{
		TxtTimeDurationFactory tfPeriod = TxtTimeDurationFactory.instance();
		
		
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
		TestTxtTimeDurationFactory cli = new TestTxtTimeDurationFactory();
		
		cli.compare();
	}
}