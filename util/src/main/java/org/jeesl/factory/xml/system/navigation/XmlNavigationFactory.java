package org.jeesl.factory.xml.system.navigation;

import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.model.xml.system.navigation.Navigation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlNavigationFactory <V extends JeeslSecurityView<?,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlNavigationFactory.class);
		
	private Navigation q;
	
	public XmlNavigationFactory(Navigation q)
	{
		this.q=q;
	}
	
	public Navigation build(V view)
	{
		Navigation xml = new Navigation();
		if(q.isSetPackage()){xml.setPackage(view.getPackageName());}
		if(q.isSetViewPattern() && view.getViewPattern()!=null){xml.setViewPattern(XmlViewPatternFactory.build(view.getViewPattern()));}
		if(q.isSetUrlMapping() && view.getUrlMapping()!=null){xml.setUrlMapping(XmlUrlMappingFactory.build(view.getUrlMapping(), view.getUrlBase()));}
		
		return xml;
	}
}