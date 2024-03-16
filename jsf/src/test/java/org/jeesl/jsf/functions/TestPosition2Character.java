package org.jeesl.jsf.functions;

import org.jeesl.jsf.function.Position2Character;
import org.jeesl.test.AbstractJeeslJsfTest;
import org.jeesl.test.JeeslJsfTestBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPosition2Character extends AbstractJeeslJsfTest
{
	final static Logger logger = LoggerFactory.getLogger(TestPosition2Character.class);
	
	
	@Test public void test()
	{
		Assertions.assertEquals("A", Position2Character.toChar(1));
		Assertions.assertEquals("B", Position2Character.toChar(2));	
	}
	
	public static void main(String[] args)
    {
		JeeslJsfTestBootstrap.init();		
    }
}