package org.jeesl.util.query.xml.module;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.model.xml.jeesl.QueryCalendar;
import org.jeesl.model.xml.module.calendar.Item;

public class XmlCalendarQuery
{
	public static enum Key {rItem}
	
	private static Map<Key,QueryCalendar> mQueries;
	
	public static QueryCalendar get(Key key){return get(key,null);}
	public static QueryCalendar get(Key key,String localeCode)
	{
		if(mQueries==null){mQueries = new HashMap<>();}
		if(!mQueries.containsKey(key))
		{
			QueryCalendar q = new QueryCalendar();
			switch(key)
			{
				case rItem: q.setItem(rItem()); break;
				
			}
			mQueries.put(key, q);
		}
		QueryCalendar q = mQueries.get(key);
		q.setLocaleCode(localeCode);
		return q;
	}
	
	private static Item rItem()
	{		
		Item xml = new Item();
		xml.setId(0);
		xml.setType(XmlTypeFactory.build("",""));
		
		return xml;
	}
}