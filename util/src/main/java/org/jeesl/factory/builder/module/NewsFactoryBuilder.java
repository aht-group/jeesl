package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,D,R,CATEGORY,?>,
								USER extends EjbWithId>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(NewsFactoryBuilder.class);
	
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory() {return cCategory;}
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem() {return cItem;}

	public NewsFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<CATEGORY> cCategory,
								final Class<ITEM> cItem)
	{       
		super(cL,cD);
		this.cCategory=cCategory;
		this.cItem=cItem;
	}

//	public EjbSystemNewsFactory<L,D,CATEGORY,NEWS,USER> news(String[] localeCodes)
//	{
//		return new EjbSystemNewsFactory<L,D,CATEGORY,NEWS,USER>(localeCodes,cL,cD,cNews);
//	}
}