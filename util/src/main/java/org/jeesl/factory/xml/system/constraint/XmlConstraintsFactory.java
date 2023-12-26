package org.jeesl.factory.xml.system.constraint;

import org.jeesl.model.xml.system.constraint.Constraints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlConstraintsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlConstraintsFactory.class);
	
	public static Constraints build()
	{
		Constraints xml = new Constraints();
		return xml;
	}
}