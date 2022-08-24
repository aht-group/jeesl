package org.jeesl.jsf.functions;

import org.jeesl.jsf.function.Position2Character;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractAhtUtilsJsfTst;
import net.sf.ahtutils.test.UtilsJsfTstBootstrap;

public class TestPosition2Character extends AbstractAhtUtilsJsfTst
{
	final static Logger logger = LoggerFactory.getLogger(TestPosition2Character.class);
	
	
	@Test
	public void test()
	{
		Assert.assertEquals("A", Position2Character.toChar(1));
		Assert.assertEquals("B", Position2Character.toChar(2));	
	}
	
	public static void main(String[] args)
    {
		UtilsJsfTstBootstrap.init();		
		
    }
}