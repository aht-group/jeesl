package net.sf.ahtutils.controller.factory.xml.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.access.Views;

public class XmlViewsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlViewsFactory.class);
	
	public static Views build()
	{
		return new Views();
	}
}