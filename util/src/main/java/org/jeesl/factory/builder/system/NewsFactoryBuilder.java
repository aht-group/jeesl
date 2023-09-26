package org.jeesl.factory.builder.system;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.system.EjbSystemNewsFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.news.JeeslSystemNews;
import org.jeesl.interfaces.model.system.news.JeeslSystemNewsCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								CATEGORY extends JeeslSystemNewsCategory<L,D,CATEGORY,?>,
								NEWS extends JeeslSystemNews<L,D,CATEGORY,NEWS,USER>,
								USER extends EjbWithId>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(NewsFactoryBuilder.class);
	
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory() {return cCategory;}
	private final Class<NEWS> cNews; public Class<NEWS> getClassNews() {return cNews;}

	public NewsFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<CATEGORY> cCategory,
								final Class<NEWS> cNews)
	{       
		super(cL,cD);
		this.cNews=cNews;
		this.cCategory=cCategory;
	}

	public EjbSystemNewsFactory<L,D,CATEGORY,NEWS,USER> news(String[] localeCodes)
	{
		return new EjbSystemNewsFactory<L,D,CATEGORY,NEWS,USER>(localeCodes,cL,cD,cNews);
	}
}