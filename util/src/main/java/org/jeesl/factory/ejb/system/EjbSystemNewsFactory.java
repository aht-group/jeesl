package org.jeesl.factory.ejb.system;

import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.news.JeeslSystemNews;
import org.jeesl.interfaces.model.system.news.JeeslSystemNewsCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSystemNewsFactory<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslSystemNewsCategory<L,D,CATEGORY,?>,
								NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
								USER extends EjbWithId>
{
	final static Logger logger = LoggerFactory.getLogger(EjbSystemNewsFactory.class);
	
	private String[] localeCodes;
	
	final Class<NEWS> cNews;
	
	private EjbLangFactory<L> efLang;
	private EjbDescriptionFactory<D> efDescription;
    
	public EjbSystemNewsFactory(String[] localeCodes, final Class<L> cL,final Class<D> cD,final Class<NEWS> cNews)
	{  
		this.localeCodes=localeCodes;
        this.cNews = cNews;
		efLang = EjbLangFactory.factory(cL);
		efDescription = EjbDescriptionFactory.factory(cD);
	}
    
	public NEWS build(CATEGORY category, USER user)
	{
		DateTime dt = new DateTime();
		NEWS ejb = null;
		try
		{
			ejb = cNews.newInstance();
			ejb.setCategory(category);
			ejb.setAuthor(user);
			ejb.setValidFrom(dt.toDate());
			ejb.setValidUntil(dt.plusMonths(1).toDate());
			ejb.setVisible(false);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}