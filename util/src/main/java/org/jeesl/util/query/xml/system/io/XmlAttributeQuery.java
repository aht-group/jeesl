package org.jeesl.util.query.xml.system.io;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.model.xml.jeesl.QueryAttribute;
import org.jeesl.model.xml.system.io.attribute.Attribute;
import org.jeesl.model.xml.system.io.attribute.Attributes;

public class XmlAttributeQuery
{
	public static enum Key {rAttributeSet,rAttributeData}
	
	private static Map<Key,QueryAttribute> mQueries;
	
	public static QueryAttribute get(Key key){return get(null,key);}
	public static QueryAttribute get(String localeCode, Key key)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,QueryAttribute>();}
		if(!mQueries.containsKey(key))
		{
			QueryAttribute q = new QueryAttribute();
			switch(key)
			{
				case rAttributeSet: q.setAttributes(rAttributeSet());break;
				case rAttributeData: q.setAttribute(rAttributeData());break;
			}
			mQueries.put(key,q);
		}
		QueryAttribute q = mQueries.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}
	
	private static Attributes rAttributeSet()
	{
		Attributes xml = new Attributes();
		
		Attribute xAttribute = new Attribute();
		xAttribute.setCode("");
		xAttribute.setLabel("");
		xml.getAttribute().add(xAttribute);
		
		return xml;
	}
	
	private static Attribute rAttributeData()
	{
		Attribute xml = new Attribute();
		xml.setCode("");		
		return xml;
	}
}