package net.sf.ahtutils.factory.xml.navigation;

import net.sf.ahtutils.xml.access.View;
import net.sf.ahtutils.xml.navigation.MenuItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMenuItemFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMenuItemFactory.class);
		
	public static MenuItem clone(MenuItem item)
	{
		MenuItem xml = new MenuItem();
		xml.setName(item.getName());
		xml.setHref(item.getHref());
		xml.setCode(item.getCode());

		return xml;
	}
	
	public static MenuItem dynamic(String dynamicCode, String urlParameter, String label)
	{
		View view = new View();
		view.setCode(dynamicCode);
		view.setUrlParameter(urlParameter);
		view.setLabel(label);

		MenuItem item = new MenuItem();
		item.setCode(dynamicCode+view.getUrlParameter());
		item.setView(view);
		return item;
	}
	
	
}