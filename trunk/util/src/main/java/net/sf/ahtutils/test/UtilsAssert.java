package net.sf.ahtutils.test;

import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.Assert;

public class UtilsAssert
{
	protected void jaxb(Object expected, Object actual)
	{
		Assert.assertEquals("XML-expected differes from XML-actual",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
}