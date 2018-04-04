package org.jeesl.factory.txt.util;

import org.jeesl.AbstractJeeslUtilTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestTxtFactory extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestTxtFactory.class);
		
	@Test public void truncate()
    {	
		String s = "12345";
		
		for(int i=0;i<7;i++)
		{
			int expected=i;
			if(i>s.length()) {expected=s.length();}
			Assert.assertEquals(expected, TxtFactory.truncate(i, s).length());
		}
    }
}