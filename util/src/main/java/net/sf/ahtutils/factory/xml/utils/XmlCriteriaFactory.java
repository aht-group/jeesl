package net.sf.ahtutils.factory.xml.utils;

import org.jeesl.model.xml.system.util.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCriteriaFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCriteriaFactory.class);
		
	public static <E extends Enum<E>> Criteria build(E code, Boolean value){return build(code.toString(),value);}
	public static <E extends Enum<E>> Criteria build(E code, Integer value){return build(code.toString(),value);}
	public static <E extends Enum<E>> Criteria build(E code, String value){return build(code.toString(),value);}
	
	public static Criteria build(String code, Boolean value){return build(code,Boolean.class.getSimpleName(),value.toString());}
	public static Criteria build(String code, Integer value){return build(code,Integer.class.getSimpleName(),value.toString());}
	public static Criteria build(String code, String value){return build(code,String.class.getSimpleName(),value.toString());}
	
	private static Criteria build(String code, String type, String value)
	{
		Criteria xml = new Criteria();
		xml.setCode(code);
		xml.setType(type);
		xml.setValue(value);
		return xml;
	}
}