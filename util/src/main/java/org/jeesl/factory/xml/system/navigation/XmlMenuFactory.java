package org.jeesl.factory.xml.system.navigation;

import java.util.HashSet;
import java.util.Set;

import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.navigation.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMenuFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMenuFactory.class);
	
	public static Menu build() {return new Menu();}
	
	public static Set<String> toCodeSet(Menu menu)
	{
		Set<String> set = new HashSet<>();
		for(MenuItem item : menu.getMenuItem())
		{
			toCodeSet(set,item);
		}
		return set;
	}
	
	private static void toCodeSet(Set<String> set, MenuItem item)
	{
		if(set.contains(item.getCode())) {logger.warn("MenuItem already exists: "+item.getCode());}
		else {set.add(item.getCode());}
		for(MenuItem child : item.getMenuItem())
		{
			toCodeSet(set,child);
		}
	}
}