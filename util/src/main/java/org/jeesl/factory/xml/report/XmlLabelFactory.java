package org.jeesl.factory.xml.report;

import org.jeesl.model.xml.io.report.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlLabelFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlLabelFactory.class);
		
	public static <E extends Enum<E>> Label build(E key, String value){return build(null,key.toString(),value);}
	public static <E extends Enum<E>> Label build(String scope, E key, String value){return build(scope,key.toString(),value);}
	public static Label build(String key, String value) {return build(null,key,value);}
	public static Label build(String scope, String key, String value)
	{
		Label xml = new Label();
		xml.setScope(scope);
		xml.setKey(key);
		xml.setValue(value);
		return xml;
	}
}