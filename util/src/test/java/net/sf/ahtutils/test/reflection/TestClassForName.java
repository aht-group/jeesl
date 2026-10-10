package net.sf.ahtutils.test.reflection;

import org.jeesl.model.xml.xsd.aht.Aht;
import org.jeesl.test.JeeslBootstrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class TestClassForName extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(TestClassForName.class);
	
	@Test
	public void test() throws ClassNotFoundException
	{
		String expected = Aht.class.getName();
		
		Class<?> clazz = Class.forName(expected);
		String actual = clazz.getName();
		
		logger.debug("Expected: "+expected);
		logger.debug("Actual: "+actual);
		
		Assertions.assertEquals(expected,actual);
	}
	
	public static void main(String args[]) throws Exception
    {
		JeeslBootstrap.init();
		
		TestClassForName test = new TestClassForName();
		test.test();
    }
}