package org.jeesl.factory.xml.system.security;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.model.xml.system.security.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								AT extends JeeslSecurityTemplate<L,D,C>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplateFactory.class);
		
	private Template q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescriptions;
	
	public XmlTemplateFactory(Template q)
	{
		this.q=q;
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}

	public Template build(AT template)
	{
		Template xml = new Template();
		if(q.isSetCode()){xml.setCode(template.getCode());}
		if(q.isSetPosition()){xml.setPosition(template.getPosition());}
		if(q.isSetVisible()){xml.setVisible(template.isVisible());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(template.getName()));}
		if(q.isSetDescriptions()) {xml.setDescriptions(xfDescriptions.create(template.getDescription()));}
		
		return xml;
	}
	
	public static Template build(String code)
	{
		Template xml = new Template();
		xml.setCode(code);
		return xml;
	}
}