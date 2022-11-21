package org.jeesl.factory.xml.module.ts;

import org.jeesl.model.xml.module.ts.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPointFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlPointFactory.class);
	
	public static <E extends Enum<E>> Point build(E code, Double value) {return build(code.toString(),value);}
	public static Point build(String code, Double value)
	{
		Point xml = new Point();
		xml.setCode(code);
		if(value!=null) {xml.setValue(value);}
		return xml;
	}
}