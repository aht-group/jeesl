package org.jeesl.factory.xml.system.io.report;

import org.jeesl.interfaces.model.io.report.JeeslIoReport;
import org.jeesl.model.xml.io.report.Footer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFooterFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFooterFactory.class);
		
	public static Footer build(JeeslIoReport.FooterOrientation orientation, String  value)
	{
		Footer xml = new Footer();
		xml.setOrientation(orientation.toString());
		xml.setValue(value);
		return xml;
	}
}