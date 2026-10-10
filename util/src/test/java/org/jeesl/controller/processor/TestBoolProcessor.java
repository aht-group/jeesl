package org.jeesl.controller.processor;

import java.util.Arrays;
import java.util.List;

import org.jeesl.AbstractJeeslUtilTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestBoolProcessor extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestBoolProcessor.class);
	
	private List<Boolean> a;

	@BeforeEach public void init()
	{		
		Boolean tmp [] = {true, false, true, true, true, false, false, true}; //AND - false , OR - true
		a = Arrays.asList(tmp);
	}
	
	@Test public void pre()
    {	
		Assertions.assertEquals(8,a.size());
    }
 

    @Test public void and()
    {
		Boolean actualA = BooleanProcessor.query("true AND false",a);
		Assertions.assertFalse(actualA);
    }
    
    @Test public void or()
    {
		Boolean actualA = BooleanProcessor.query("true OR false",a);
		Assertions.assertTrue(actualA);
    }
}