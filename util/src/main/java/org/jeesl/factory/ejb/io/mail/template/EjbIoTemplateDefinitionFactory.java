package org.jeesl.factory.ejb.io.mail.template;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
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

public class EjbIoTemplateDefinitionFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslStatus<L,D,CATEGORY>,
								CHANNEL extends JeeslTemplateChannel<L,D,CHANNEL,?>,
								TEMPLATE extends JeeslIoTemplate<L,D,CATEGORY,SCOPE,DEFINITION,TOKEN>,
								SCOPE extends JeeslStatus<L,D,SCOPE>,
								DEFINITION extends JeeslIoTemplateDefinition<D,CHANNEL,TEMPLATE>,
								TOKEN extends JeeslIoTemplateToken<L,D,TEMPLATE,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoTemplateDefinitionFactory.class);
	
	final Class<DEFINITION> cDefinition;
	
	private EjbDescriptionFactory<D> efDescription;
    
	public EjbIoTemplateDefinitionFactory(final Class<D> cD,final Class<DEFINITION> cDefinition)
	{       
        this.cDefinition = cDefinition;
		efDescription = EjbDescriptionFactory.factory(cD);
	}
	
	public DEFINITION build(TEMPLATE template, CHANNEL type, Entity xml)
	{
		DEFINITION ejb = build(template,type);
		try
		{
			ejb.setDescription(efDescription.create(xml.getDescriptions()));
		}
		catch (JeeslConstraintViolationException e) {e.printStackTrace();}
		return ejb;
	}
    
	public DEFINITION build(TEMPLATE template, CHANNEL type)
	{
		DEFINITION ejb = null;
		try
		{
			ejb = cDefinition.newInstance();
			ejb.setTemplate(template);
			ejb.setType(type);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}