package net.sf.ahtutils.factory.xml.status;

import org.jeesl.model.xml.io.locale.status.Categories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCategoriesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCategoriesFactory.class);
	
	public static Categories build()
	{
		Categories xml = new Categories();
		
		return xml;
	}
}