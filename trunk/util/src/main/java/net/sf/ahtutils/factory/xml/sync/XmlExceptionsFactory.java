package net.sf.ahtutils.factory.xml.sync;

import net.sf.ahtutils.xml.sync.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlExceptionsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlExceptionsFactory.class);
			
	public static Exceptions build()
	{
		Exceptions xml = new Exceptions();
		
		return xml;
	}
}