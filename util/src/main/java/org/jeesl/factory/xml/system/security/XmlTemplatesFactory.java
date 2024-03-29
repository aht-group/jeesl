package org.jeesl.factory.xml.system.security;

import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.model.xml.system.security.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplatesFactory <L extends JeeslLang, D extends JeeslDescription, 
								C extends JeeslSecurityCategory<L,D>,
								AT extends JeeslSecurityTemplate<L,D,C>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplatesFactory.class);
		
	private Templates q;
	
	private XmlTemplateFactory<L,D,C,AT> xfTemplate;
	
	public XmlTemplatesFactory(Templates q)
	{
		this.q=q;
		if(Objects.nonNull(q.getTemplate())) {xfTemplate = new XmlTemplateFactory<>(q.getTemplate().get(0));}
	}
	
	public Templates build(List<AT> templates)
	{
		Templates xml = build();
		if(Objects.nonNull(q.getTemplate()))
		{
			for(AT template : templates)
			{
				xml.getTemplate().add(xfTemplate.build(template));
			}
		}

		return xml;
	}
	
	public static Templates build()
	{
		return new Templates();
	}

}