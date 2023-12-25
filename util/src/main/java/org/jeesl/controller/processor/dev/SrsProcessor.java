package org.jeesl.controller.processor.dev;

import java.util.List;

import org.jeesl.factory.xml.dev.srs.XmlSrsFactory;
import org.jeesl.model.xml.dev.srs.Srs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SrsProcessor
{
	final static Logger logger = LoggerFactory.getLogger(SrsProcessor.class);
	
	public static Srs merge(List<Srs> srs)
	{
		Srs result = XmlSrsFactory.build();
		
		
		
		return result;
	}
}