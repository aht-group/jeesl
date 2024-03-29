package org.jeesl.factory.builder.module;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.news.EjbNewsFeedFactory;
import org.jeesl.factory.ejb.module.news.EjbNewsItemFactory;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsFactoryBuilder<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>,
								FEED extends JeeslNewsFeed<L,D,R>,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,FEED,CATEGORY,USER,M,?>,
								USER extends JeeslSimpleUser,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>>
		extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(NewsFactoryBuilder.class);
	
	private final Class<LOC> cLocale; public Class<LOC> getClassLocale() {return cLocale;}
	private final Class<R> cRealm; public Class<R> getClassRealm() {return cRealm;}
	private final Class<FEED> cFeed; public Class<FEED> getClassFeed() {return cFeed;}
	private final Class<CATEGORY> cCategory; public Class<CATEGORY> getClassCategory() {return cCategory;}
	private final Class<ITEM> cItem; public Class<ITEM> getClassItem() {return cItem;}
	
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}

	public NewsFactoryBuilder(final Class<L> cL,final Class<D> cD, Class<LOC> cLocale,
								final Class<R> cRealm,
								final Class<FEED> cFeed,
								final Class<CATEGORY> cCategory,
								final Class<ITEM> cItem,
								final Class<M> cMarkup,
								final Class<MT> cMarkupType)
	{       
		super(cL,cD);
		this.cLocale=cLocale;
		this.cRealm=cRealm;
		this.cFeed=cFeed;
		this.cCategory=cCategory;
		this.cItem=cItem;
		this.cMarkup=cMarkup;
		this.cMarkupType=cMarkupType;
	}

	public EjbNewsFeedFactory<L,D,R,FEED> ejbFeed() {return new EjbNewsFeedFactory<>(cFeed);}
	public EjbNewsItemFactory<L,D,LOC,R,FEED,CATEGORY,ITEM,USER,M,MT> ejbItem() {return new EjbNewsItemFactory<>(cL,cItem,cMarkup);}
}