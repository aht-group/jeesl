package org.jeesl.factory.xml.system.security;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.navigation.XmlNavigationFactory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.security.View;

public class XmlViewFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,?,?,?>,
								V extends JeeslSecurityView<L,D,C,R,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlViewFactory.class);
		
	private View q;
	private net.sf.ahtutils.xml.access.View qOld;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlNavigationFactory<V> xfNavigation;
	
	public XmlViewFactory(net.sf.ahtutils.xml.access.View q)
	{
		this.qOld=q;
	}
	
	public XmlViewFactory(View q)
	{
		this.q=q;
		if(q.isSetLangs()){xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetNavigation()) {xfNavigation = new XmlNavigationFactory<>(q.getNavigation());}
	}

	public View build(V view)
	{
		View xml = new View();
		if(q.isSetCode()){xml.setCode(view.getCode());}
		if(q.isSetPosition()){xml.setPosition(view.getPosition());}
		if(q.isSetVisible()){xml.setVisible(view.isVisible());}
		if(q.isSetDocumentation() && view.getDocumentation()!=null){xml.setDocumentation(view.getDocumentation());}
		
		if(q.isSetLangs()){xml.setLangs(xfLangs.getUtilsLangs(view.getName()));}
		if(q.isSetDescriptions()){xml.setDescriptions(xfDescription.create(view.getDescription()));}
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
	
	public net.sf.ahtutils.xml.access.View create(V view)
	{
		net.sf.ahtutils.xml.access.View xml = new net.sf.ahtutils.xml.access.View();
		
		if(qOld.isSetCode()){xml.setCode(view.getCode());}
		if(qOld.isSetPosition()){xml.setPosition(view.getPosition());}
		if(qOld.isSetVisible()){xml.setVisible(view.isVisible());}
		if(qOld.isSetDocumentation() && view.getDocumentation()!=null){xml.setDocumentation(view.getDocumentation());}
		
		if(qOld.isSetLangs())
		{
			XmlLangsFactory<L> f = new XmlLangsFactory<L>(qOld.getLangs());
			xml.setLangs(f.getUtilsLangs(view.getName()));
		}
		
		if(qOld.isSetDescriptions())
		{
			XmlDescriptionsFactory<D> f = new XmlDescriptionsFactory<D>(qOld.getDescriptions());
			xml.setDescriptions(f.create(view.getDescription()));
		}
		
		if(qOld.isSetPublic() && view.getAccessPublic()!=null){xml.setPublic(view.getAccessPublic());}
		if(qOld.isSetOnlyLoginRequired() && view.getAccessLogin()!=null){xml.setOnlyLoginRequired(view.getAccessLogin());}
		
		return xml;
	}
	
	public static net.sf.ahtutils.xml.access.View create(String code)
	{
		net.sf.ahtutils.xml.access.View xml = new net.sf.ahtutils.xml.access.View();
		xml.setCode(code);
		return xml;
	}
}