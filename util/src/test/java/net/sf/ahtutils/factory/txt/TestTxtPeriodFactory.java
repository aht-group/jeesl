package net.sf.ahtutils.factory.txt;

import org.jeesl.factory.txt.module.calendar.TxtPeriodJodaFactory;
import org.jeesl.test.JeeslBootstrap;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class TestTxtPeriodFactory extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtPeriodFactory.class);
	
	private TxtPeriodJodaFactory tfPeriod;
	
	@Before
	public void init()
	{
		tfPeriod = new TxtPeriodJodaFactory();
	}
 
    @Test
    public void hours()
    {	
//    	String expected = "Now Testing: TestTxtDataUpdateFactory";
    	String actual = tfPeriod.debug(3125);
    	logger.debug(actual);
//    	Assert.assertEquals(expected, actual);
    }
       
	public static void main (String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		TestTxtPeriodFactory test = new TestTxtPeriodFactory();
		test.init();
		
		test.hours();
	}
}