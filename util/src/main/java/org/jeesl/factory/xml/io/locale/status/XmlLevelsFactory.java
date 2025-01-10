package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Level;
import org.jeesl.model.xml.io.locale.status.Levels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLevelsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlLevelsFactory.class);
		
	public static Levels build()
	{
		return new Levels();
	}
	
	public static Levels build(Level level)
	{
		Levels xml = build();
		xml.getLevel().add(level);
		return xml;
	}
}