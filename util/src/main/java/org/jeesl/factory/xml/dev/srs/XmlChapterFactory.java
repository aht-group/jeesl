package org.jeesl.factory.xml.dev.srs;

import org.jeesl.model.xml.dev.srs.Chapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlChapterFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlChapterFactory.class);
	
	public static Chapter build(String title)
    {
    	Chapter xml = build();
    	xml.setTitle(title);
    	return xml;
    }
	
    public static Chapter build()
    {
    	Chapter xml = new Chapter();
    	return xml;
    }
}