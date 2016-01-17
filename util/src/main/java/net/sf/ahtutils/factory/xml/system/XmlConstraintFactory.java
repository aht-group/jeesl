package net.sf.ahtutils.factory.xml.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.system.Constraint;

public class XmlConstraintFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlConstraintFactory.class);
	
	public static Constraint build()
	{
		Constraint xml = new Constraint();
		return xml;
	}
}