package org.jeesl.controller.processor;

import org.jeesl.AbstractJeeslUtilTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRestrictedCharacterProcessor extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestRestrictedCharacterProcessor.class);
	
	@Test
	public void simpleStatic()
	{
		String input = "a b";
		String actual = RestrictedCharacterProcessor.prettyUrl(input);
		Assert.assertEquals("a-b", actual);
	}
	
	@Test
	public void specialChars()
	{
		Assert.assertEquals("a-b", RestrictedCharacterProcessor.prettyUrl("a/b"));
	}
	
	@Test
	public void simpleObject()
	{
		String input = "a b";
		
		RestrictedCharacterProcessor pu = new RestrictedCharacterProcessor();
		
		String actual = pu.url(input);
		Assert.assertEquals("a-b", actual);
	}
	
	@Test
	public void dynamicBlank()
	{
		String input = "a b";
		
		RestrictedCharacterProcessor pu = new RestrictedCharacterProcessor();
		pu.setBlankReplace("_");
		
		String actual = pu.url(input);
		Assert.assertEquals("a_b", actual);
	}
}