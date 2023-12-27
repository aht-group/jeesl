package org.jeesl.util.query.xml;

import java.util.Hashtable;
import java.util.Map;

import org.jeesl.factory.xml.system.status.XmlScopeFactory;
import org.jeesl.model.xml.system.util.TrafficLight;
import org.jeesl.model.xml.system.util.TrafficLights;

import net.sf.ahtutils.factory.xml.utils.XmlTrafficLightsFactory;
import net.sf.ahtutils.xml.aht.Query;

public class UtilsQuery
{
	public static enum Key {exTrafficLights,exTrafficLight}
	
	private static Map<Key,Query> mQueries;
	
	public static Query get(Key key){return get(key,null);}
	public static Query get(Key key,String lang)
	{
		if(mQueries==null){mQueries = new Hashtable<Key,Query>();}
		if(!mQueries.containsKey(key))
		{
			Query q = new Query();
			switch(key)
			{
				case exTrafficLights: q.setTrafficLights(exTrafficLights());break;
				case exTrafficLight: q.setTrafficLight(exTrafficLight());break;
			}
			mQueries.put(key, q);
		}
		Query q = mQueries.get(key);
		q.setLang(lang);
		return q;
	}
	
	public static TrafficLights exTrafficLights()
	{		
		TrafficLights xml = XmlTrafficLightsFactory.build();
		xml.getTrafficLight().add(exTrafficLight());
		return xml;
	}
	
	public static TrafficLight exTrafficLight()
	{		
		TrafficLight xml = new TrafficLight();
		xml.setId(0);
		xml.setThreshold(0);
		xml.setScope(XmlScopeFactory.build(""));
		
		xml.setColorBackground("");
		xml.setColorText("");
		
		xml.setLangs(XmlStatusQuery.langs());
		xml.setDescriptions(XmlStatusQuery.descriptions());
		
		return xml;
	}
}
