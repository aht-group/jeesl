package net.sf.ahtutils.controller.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsPasswordGenerator
{
	final static Logger logger = LoggerFactory.getLogger(UtilsPasswordGenerator.class);
	
	public static String random()
	{
		return random(10);
	}
	
	public static String random(int size)
	{
		return RandomStringUtils.randomAlphabetic(size);
	}
}