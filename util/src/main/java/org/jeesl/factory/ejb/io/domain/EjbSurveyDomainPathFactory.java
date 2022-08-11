package org.jeesl.factory.ejb.io.domain;

import java.util.List;

import org.jeesl.interfaces.model.io.domain.JeeslDomainPath;
import org.jeesl.interfaces.model.io.domain.JeeslDomainQuery;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSurveyDomainPathFactory<L extends JeeslLang, D extends JeeslDescription,
										QUERY extends JeeslDomainQuery<L,D,?,PATH>,
										PATH extends JeeslDomainPath<L,D,QUERY,DENTITY,DATTRIBUTE>,
										DENTITY extends JeeslRevisionEntity<L,D,?,?,?,?>,
										DATTRIBUTE extends JeeslRevisionAttribute<L,D,DENTITY,?,?>
										>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSurveyDomainPathFactory.class);
	
	private final Class<PATH> cPath;
    
	public EjbSurveyDomainPathFactory(final Class<PATH> cPath)
	{       
        this.cPath = cPath;
	}
    
	public PATH build(QUERY query, DENTITY entity, List<PATH> list)
	{
		PATH ejb = null;
		try
		{
			ejb = cPath.newInstance();
			ejb.setQuery(query);
			ejb.setEntity(entity);
			if(list==null) {ejb.setPosition(1);}
			else {ejb.setPosition(list.size()+1);}
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}