package org.jeesl.test;

import org.exlp.util.jx.JaxbUtil;
import org.junit.jupiter.api.Assertions;

public class JeeslAssert
{
	public void jaxb(Object expected, Object actual)
	{
		Assertions.assertEquals(JaxbUtil.toString(expected),JaxbUtil.toString(actual),"XML-expected differes from XML-actual");
	}
}