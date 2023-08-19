package org.jeesl.factory.xml.system.security;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.controller.util.comparator.ejb.system.security.SecurityViewComparator;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Views;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlViewsFactory <L extends JeeslLang,
								D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,?,?>,
								V extends JeeslSecurityView<L,D,C,R,U,?>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlViewsFactory.class);
		
	private Comparator<V> comparator;
	private XmlViewFactory<L,D,C,R,V> xfView;
	
	public XmlViewsFactory(Views q)
	{
		comparator = (new SecurityViewComparator<V>()).factory(SecurityViewComparator.Type.position);
		xfView = new XmlViewFactory<L,D,C,R,V>(q.getView().get(0));
	}

	public Views build(List<V> views)
	{
		Views xml = build();
		Collections.sort(views,comparator);
		for(V view : views)
		{
			xml.getView().add(xfView.build(view));
		}
		return xml;
	}
	
	public static Views build()
	{
		return new Views();
	}
}