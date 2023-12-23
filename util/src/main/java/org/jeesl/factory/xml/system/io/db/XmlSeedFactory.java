package org.jeesl.factory.xml.system.io.db;

import org.jeesl.model.xml.io.db.Seed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSeedFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSeedFactory.class);
	
	public static Seed build(String code, String content)
	{
		Seed xml = build(code);
		xml.setContent(content);
		return xml;
	}
	
	public static Seed build(String code)
	{
		Seed xml = new Seed();
		xml.setCode(code);
		return xml;
	}
}