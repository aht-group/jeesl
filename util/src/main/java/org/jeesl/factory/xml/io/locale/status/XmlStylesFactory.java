package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Style;
import org.jeesl.model.xml.io.locale.status.Styles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStylesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlStylesFactory.class);
		
	public static Styles build()
	{
		Styles xml = new Styles();

		return xml;
	}
	
	public static Styles build(Style style)
	{
		Styles xml = new Styles();
		xml.getStyle().add(style);
		return xml;
	}
}