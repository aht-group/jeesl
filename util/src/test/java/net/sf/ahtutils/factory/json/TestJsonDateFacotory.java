package net.sf.ahtutils.factory.json;

import java.time.LocalDate;
import java.util.Date;

import org.exlp.util.system.DateUtil;
import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.factory.json.util.JsonDateFactory;
import org.jeesl.test.JeeslBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.model.json.date.JsonDateYQM;
import net.sf.ahtutils.model.json.date.JsonDateYQMW;
import net.sf.ahtutils.model.json.date.JsonDateYQMWD;

public class TestJsonDateFacotory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestJsonDateFacotory.class);
	
    @Test public void values()
    {	
    	JsonDateYQMWD json = new JsonDateYQMWD();
    	JsonDateFactory.build(json, DateUtil.toDate(LocalDate.of(2016,5,2)));
    	Assertions.assertEquals(2016,json.getYear());
    	Assertions.assertEquals("Q2",json.getQuarter());
    	Assertions.assertEquals(5,json.getMonth());
    	Assertions.assertEquals(18,json.getWeek());
    	Assertions.assertEquals(2,json.getDay());
    }
    
    @Test public void yearQuarterMonthWeekDay()
    {	
    	JsonDateYQMWD json = new JsonDateYQMWD();
    	JsonDateFactory.build(json, new Date());
    	Assertions.assertNotEquals(0,json.getYear());
    	Assertions.assertNotNull(json.getQuarter());
    	Assertions.assertNotEquals(0,json.getMonth());
    	Assertions.assertNotEquals(0,json.getWeek());
    	Assertions.assertNotEquals(0,json.getDay());
    }
    
    @Test public void yearQuarterMonthWeek()
    {	
    	JsonDateYQMW json = new JsonDateYQMW();
    	JsonDateFactory.build(json, new Date());
    	Assertions.assertNotEquals(0,json.getYear());
    	Assertions.assertNotNull(json.getQuarter());
    	Assertions.assertNotEquals(0,json.getMonth());
    	Assertions.assertNotEquals(0,json.getWeek());
    }
    
    @Test public void yearQuarterMonth()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assertions.assertNotEquals(0,json.getYear());
    	Assertions.assertNotNull(json.getQuarter());
    	Assertions.assertNotEquals(0,json.getMonth());
    }
    
    @Test public void yearQuarter()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assertions.assertNotEquals(0,json.getYear());
    	Assertions.assertNotNull(json.getQuarter());
    }
    
    @Test public void year()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assertions.assertNotEquals(0,json.getYear());
    }
       
	public static void main (String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		TestJsonDateFacotory test = new TestJsonDateFacotory();
		test.yearQuarterMonthWeekDay();
	}
}