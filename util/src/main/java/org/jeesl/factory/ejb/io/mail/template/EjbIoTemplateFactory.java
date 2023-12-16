package org.jeesl.factory.ejb.io.mail.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateToken;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.label.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoTemplateFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
								TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
								SCOPE extends JeeslStatus<L,D,SCOPE>,
								DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
								TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoTemplateFactory.class);
	
	final Class<TEMPLATE> cTemplate;
	
	private EjbLangFactory<L> efLang;
	private EjbDescriptionFactory<D> efDescription;
    
	public EjbIoTemplateFactory(final Class<L> cL,final Class<D> cD,final Class<TEMPLATE> cTemplate)
	{       
        this.cTemplate = cTemplate;
		efLang = EjbLangFactory.instance(cL);
		efDescription = EjbDescriptionFactory.factory(cD);
	}
	
	public TEMPLATE build(CATEGORY category, Entity xml)
	{
		TEMPLATE ejb = build(category);
		ejb.setCode(xml.getCode());
		ejb.setPosition(xml.getPosition());
		try
		{
			ejb.setName(efLang.getLangMap(xml.getLangs()));
			ejb.setDescription(efDescription.create(xml.getDescriptions()));
		}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		return ejb;
	}
    
	public TEMPLATE build(CATEGORY category)
	{
		TEMPLATE ejb = null;
		try
		{
			ejb = cTemplate.newInstance();
			ejb.setCategory(category);
			ejb.setPosition(1);
			ejb.setVisible(true);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public Map<String,TEMPLATE> buildMap(List<TEMPLATE> templates)
	{
		Map<String,TEMPLATE> map = new HashMap<String,TEMPLATE>();
		for(TEMPLATE t : templates){map.put(t.getCode(),t);}
		return map;
	}
}