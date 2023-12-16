package org.jeesl.factory.xml.system.io.revision;

import org.jeesl.model.xml.io.label.Diagrams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDiagramsFactory
{	
	final static Logger logger = LoggerFactory.getLogger(XmlDiagramsFactory.class);
	
	public static Diagrams build()
	{
		return new Diagrams();
	}
}