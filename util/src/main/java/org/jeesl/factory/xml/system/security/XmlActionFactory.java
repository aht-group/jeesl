package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.Langs;

public class XmlActionFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								R extends JeeslSecurityRole<L,D,C,V,U,A>,
								V extends JeeslSecurityView<L,D,C,R,U,A>,
								U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
								A extends JeeslSecurityAction<L,D,R,V,U,AT>,
								AT extends JeeslSecurityTemplate<L,D,C>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActionFactory.class);
		
	private org.jeesl.model.xml.system.security.Action q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlViewFactory<L,D,C,R,V> xfView;
	private XmlTemplateFactory<L,D,C,AT> xfTemplate;
	
	public XmlActionFactory(org.jeesl.model.xml.system.security.Action q)
	{
		this.q=q;
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<D>(q.getDescriptions());}
		if(q.isSetView()) {xfView = new XmlViewFactory<>(q.getView());}
		if(q.isSetTemplate()){xfTemplate= new XmlTemplateFactory<>(q.getTemplate());}
	}
	

	public org.jeesl.model.xml.system.security.Action build(A action)
	{
		Action xml = new Action();
		if(q.isSetCode()){xml.setCode(action.getCode());}
		if(q.isSetPosition()){xml.setPosition(action.getPosition());}
		if(q.isSetVisible()){xml.setVisible(action.isVisible());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(action.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(action.getDescription()));}
		if(q.isSetView()) {xml.setView(xfView.build(action.getView()));}
		
		if(q.isSetTemplate() && action.getTemplate()!=null) {xml.setTemplate(xfTemplate.build(action.getTemplate()));}
		
		return xml;
	}
	
	public static org.jeesl.model.xml.system.security.Action build(String code)
	{
		org.jeesl.model.xml.system.security.Action xml = new org.jeesl.model.xml.system.security.Action();
		xml.setCode(code);
		return xml;
	}
	
	
	public static Langs toLangs(org.jeesl.model.xml.system.security.Action action)
	{		
		if(action.getTemplate()==null) {return action.getLangs();}
		else {return action.getTemplate().getLangs();}
	}
	

	public static Descriptions toDescriptions(org.jeesl.model.xml.system.security.Action action)
	{		
		if(action.getTemplate()==null) {return action.getDescriptions();}
		else {return action.getTemplate().getDescriptions();}
	}
}