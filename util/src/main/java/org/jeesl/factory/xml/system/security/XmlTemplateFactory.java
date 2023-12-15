package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
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
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<L>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescriptions = new XmlDescriptionsFactory<D>(q.getDescriptions());}
	}

	public Template build(AT ejb)
	{
		Template xml = new Template();
		if(Objects.nonNull(q.getCode())) {xml.setCode(ejb.getCode());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(ejb.getPosition());}
		if(Objects.nonNull(q.isVisible())) {xml.setVisible(ejb.isVisible());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(ejb.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescriptions.create(ejb.getDescription()));}
		
		return xml;
	}
	
	public static Template build(String code)
	{
		Template xml = new Template();
		xml.setCode(code);
		return xml;
	}
}