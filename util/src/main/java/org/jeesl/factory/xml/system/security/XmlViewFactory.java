package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.navigation.XmlNavigationFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlViewFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,?,?>,
								V extends JeeslSecurityView<L,D,C,R,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlViewFactory.class);
		
	private View q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlNavigationFactory<V> xfNavigation;
	
	public XmlViewFactory(View q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(Objects.nonNull(q.getNavigation())) {xfNavigation = new XmlNavigationFactory<>(q.getNavigation());}
	}

	public View build(V view)
	{
		View xml = new View();
		if(Objects.nonNull(q.getCode())) {xml.setCode(view.getCode());}
		if(q.isSetPosition()){xml.setPosition(view.getPosition());}
		if(q.isSetVisible()){xml.setVisible(view.isVisible());}
		if(q.isSetDocumentation() && view.getDocumentation()!=null) {xml.setDocumentation(view.getDocumentation());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(view.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescription.create(view.getDescription()));}
		if(q.isSetNavigation()){xml.setNavigation(xfNavigation.build(view));}
		if(q.isSetAccess()){xml.setAccess(XmlAccessFactory.build(view.getAccessPublic(), view.getAccessPublic()));}
		
		return xml;
	}
	
	public static View build(String code)
	{
		View xml = new View();
		xml.setCode(code);
		return xml;
	}
}