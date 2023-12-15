package net.sf.ahtutils.factory.xml.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlClassFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlClassFactory.class);
		
	public static org.jeesl.model.xml.io.locale.status.Class create(String code)
	{
		org.jeesl.model.xml.io.locale.status.Class xml = new org.jeesl.model.xml.io.locale.status.Class();
		xml.setCode(code);
		return xml;
	}
}