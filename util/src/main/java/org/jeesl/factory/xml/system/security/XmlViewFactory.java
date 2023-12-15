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

	public View build(V ejb)
	{
		View xml = new View();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		if(q.isSetDocumentation() && ejb.getDocumentation()!=null) {xml.setDocumentation(ejb.getDocumentation());}
		
		if(Objects.nonNull(q.getLangs())){xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())){xml.setDescriptions(xfDescription.create(ejb.getDescription()));}
		if(Objects.nonNull(q.getNavigation())) {xml.setNavigation(xfNavigation.build(ejb));}
		if(Objects.nonNull(q.getAccess())) {xml.setAccess(XmlAccessFactory.build(ejb.getAccessPublic(), ejb.getAccessPublic()));}
		
		return xml;
	}
	
	public static View build(String code)
	{
		View xml = new View();
		xml.setCode(code);
		return xml;
	}
}