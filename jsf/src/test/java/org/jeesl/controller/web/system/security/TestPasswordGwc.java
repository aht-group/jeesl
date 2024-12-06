package org.jeesl.controller.web.system.security;

import org.jeesl.test.AbstractJeeslJsfTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPasswordGwc extends AbstractJeeslJsfTest
{
	final static Logger logger = LoggerFactory.getLogger(TestPasswordGwc.class);
	
	private JeeslPasswordGwc<?,?,?> checker;
	
	public TestPasswordGwc()
	{
		checker = new JeeslPasswordGwc<>(null);
	}
	
	@Test public void length()
	{
		Assertions.assertEquals(true,checker.validLength("abc", 3));
		Assertions.assertEquals(false,checker.validLength("abc", 4));
	}
	
	@Test public void digits()
	{
		Assertions.assertEquals(true,checker.validDigits("a1b2c", 2));
		Assertions.assertEquals(true,checker.validDigits("abcdefghijklmn12", 2));
		Assertions.assertEquals(true,checker.validDigits("11abcdefghijklmn", 2));
		Assertions.assertEquals(false,checker.validDigits("ab1c", 2));
	}
	
	@Test public void lower()
	{
		Assertions.assertEquals(true,checker.validLower("abc", 2));
		Assertions.assertEquals(true,checker.validLower("ABCDEFGHIKLMNxy", 2));
		Assertions.assertEquals(true,checker.validLower("ABCDEFGHIKLMNxy", 2));
		Assertions.assertEquals(true,checker.validLower("xABCDEFGHIKLMNx", 2));
		Assertions.assertEquals(true,checker.validLower("AxxBCDEFGHIKLMN", 2));
		Assertions.assertEquals(true,checker.validLower("ABxCDEFGHIKxLMN", 2));
		Assertions.assertEquals(false,checker.validLower("ABCDEFGHIKLMNxy", 3));
		Assertions.assertEquals(true,checker.validLower("ABCDEFGHIKLMNxyy", 3));
		Assertions.assertEquals(false,checker.validLower("AbC", 2));
	}
	
	@Test public void upper()
	{
		Assertions.assertEquals(true,checker.validUpper("ABC", 2));
		Assertions.assertEquals(false,checker.validUpper("Abc", 2));
		Assertions.assertEquals(false,checker.validUpper("Abcksdfjnvgkjwnv", 2));
		Assertions.assertEquals(true,checker.validUpper("AbcksdfjBvgkjwnv", 2));
	}
	
	@Test public void symbols()
	{
		Assertions.assertEquals(true,checker.validSymbols("a-bc*", 2));
		Assertions.assertEquals(false,checker.validSymbols("ab=c", 2));
	}
}