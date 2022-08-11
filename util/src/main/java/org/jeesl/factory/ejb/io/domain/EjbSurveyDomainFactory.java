package org.jeesl.factory.ejb.io.domain;

import java.util.List;

import org.jeesl.interfaces.model.io.domain.JeeslDomain;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveyDomainFactory<L extends JeeslLang, D extends JeeslDescription,	
				DOMAIN extends JeeslDomain<L,DENTITY>,
				DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyDomainFactory.class);
	
	private final Class<DOMAIN> cDomain;
    
	public EjbSurveyDomainFactory(final Class<DOMAIN> cDomain)
	{       
        this.cDomain = cDomain;
	}
    
	public DOMAIN build(DENTITY entity, List<DOMAIN> list)
	{
		DOMAIN ejb = null;
		try
		{
			ejb = cDomain.newInstance();
//			ejb.setEntity(entity);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}