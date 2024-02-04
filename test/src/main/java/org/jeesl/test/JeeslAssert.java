package org.jeesl.test;

import org.exlp.util.jx.JaxbUtil;
import org.junit.Assert;

public class JeeslAssert
{
	public void jaxb(Object expected, Object actual)
	{
		Assert.assertEquals("XML-expected differes from XML-actual",JaxbUtil.toString(expected),JaxbUtil.toString(actual));
	}
}