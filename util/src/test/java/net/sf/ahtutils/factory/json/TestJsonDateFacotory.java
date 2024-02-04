package net.sf.ahtutils.factory.json;

import java.time.LocalDate;
import java.util.Date;

import org.exlp.util.system.DateUtil;
import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.JeeslBootstrap;
import org.jeesl.factory.json.util.JsonDateFactory;
import org.junit.Assert;
import org.junit.Test;
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
    	Assert.assertEquals(2016, json.getYear());
    	Assert.assertEquals("Q2",json.getQuarter());
    	Assert.assertEquals(5,json.getMonth());
    	Assert.assertEquals(18,json.getWeek());
    	Assert.assertEquals(2,json.getDay());
    }
    
    @Test public void yearQuarterMonthWeekDay()
    {	
    	JsonDateYQMWD json = new JsonDateYQMWD();
    	JsonDateFactory.build(json, new Date());
    	Assert.assertNotEquals(0, json.getYear());
    	Assert.assertNotNull(json.getQuarter());
    	Assert.assertNotEquals(0,json.getMonth());
    	Assert.assertNotEquals(0,json.getWeek());
    	Assert.assertNotEquals(0,json.getDay());
    }
    
    @Test public void yearQuarterMonthWeek()
    {	
    	JsonDateYQMW json = new JsonDateYQMW();
    	JsonDateFactory.build(json, new Date());
    	Assert.assertNotEquals(0, json.getYear());
    	Assert.assertNotNull(json.getQuarter());
    	Assert.assertNotEquals(0,json.getMonth());
    	Assert.assertNotEquals(0,json.getWeek());
    }
    
    @Test public void yearQuarterMonth()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assert.assertNotEquals(0, json.getYear());
    	Assert.assertNotNull(json.getQuarter());
    	Assert.assertNotEquals(0,json.getMonth());
    }
    
    @Test public void yearQuarter()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assert.assertNotEquals(0, json.getYear());
    	Assert.assertNotNull(json.getQuarter());
    }
    
    @Test public void year()
    {	
    	JsonDateYQM json = new JsonDateYQM();
    	JsonDateFactory.build(json, new Date());
    	Assert.assertNotEquals(0, json.getYear());
    }
       
	public static void main (String[] args) throws Exception
	{
		JeeslBootstrap.init();
		
		TestJsonDateFacotory test = new TestJsonDateFacotory();
		test.yearQuarterMonthWeekDay();
	}
}