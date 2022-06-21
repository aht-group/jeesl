package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.news.EjbNewsItemFactory;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,D,R,CATEGORY,USER>,
								USER extends EjbWithId>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(NewsFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory() {return cCategory;}
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem() {return cItem;}

	public NewsFactoryBuilder(final Class<L> cL,final Class<D> cD, Class<LOC> cLocale,
								final Class<CATEGORY> cCategory,
								final Class<ITEM> cItem)
	{       
		super(cL,cD);
		this.cLocale=cLocale;
		this.cCategory=cCategory;
		this.cItem=cItem;
	}

	public EjbNewsItemFactory<L,D,LOC,R,CATEGORY,ITEM,USER> ejbItem()
	{
		return new EjbNewsItemFactory<>(cL,cItem);
	}
}