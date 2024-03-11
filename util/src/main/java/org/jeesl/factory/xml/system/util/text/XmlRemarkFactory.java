package org.jeesl.factory.xml.system.util.text;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.model.xml.io.cms.text.Remark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRemarkFactory<L extends JeeslLang>
{
	final static Logger logger = LoggerFactory.getLogger(XmlRemarkFactory.class);
	
	private final String localeCode;
	
	public XmlRemarkFactory(String localeCode)
	{
		this.localeCode=localeCode;
	}
	
	public  Remark build(Map<String,L> map)
	{
		if(map.containsKey(localeCode))
		{
			return build(map.get(localeCode).getLang());
		}
		return build("--no-translation--");
	}
	
	public static Remark build(String value) {return build(null,null,value);}
	
	public static <E extends Enum<E>> Remark build(E key, String value) {return build(key.toString(),value);}
	public static <C extends EjbWithCode> Remark build(C code, String value) {return build(code.getCode(),value);}
	public static Remark build(String key,String value) {return build(key,null,value);}
	
	public static Remark build(String key,Integer version,String value)
	{
		Remark xml = new Remark();
		if(key!=null){xml.setKey(key);}
		if(version!=null){xml.setVersion(version);}
		xml.setValue(value);
		return xml;
	}
	
	public static Remark build(List<String> values, String join) {return build(null,null,StringUtils.join(values,join));}
}