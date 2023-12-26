package org.jeesl.factory.xml.system.io.report;

import org.jeesl.model.xml.io.report.Offset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOffsetFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlOffsetFactory.class);
	
	public static Offset build()
	{
		Offset xml = new Offset();
						
		return xml;
	}
	
	public static Offset build(int rows, int columns)
	{
		Offset xml = build();
		xml.setRows(rows);
		xml.setColumns(columns);
		return xml;
	}
}