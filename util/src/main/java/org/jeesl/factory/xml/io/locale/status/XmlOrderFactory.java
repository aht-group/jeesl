package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlOrderFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlOrderFactory.class);
		
	public static Order create(String code)
	{
		Order xml = new Order();
		xml.setCode(code);
		return xml;
	}
}