package org.jeesl.controller.rest.h.module.news;

import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.api.rest.i.module.news.JeeslNewsRestInterface;
import org.jeesl.controller.rest.g.module.news.JeeslNewsGenericRestHandler;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.news.NewsCategory;
import org.jeesl.model.ejb.module.news.NewsFeed;
import org.jeesl.model.ejb.module.news.NewsItem;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslNewsRestHandler extends JeeslNewsGenericRestHandler<TenantRealm,NewsFeed,NewsCategory,NewsItem> implements JeeslNewsRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(JeeslNewsRestHandler.class);

	public JeeslNewsRestHandler(JeeslNewsFacade<?,?,TenantRealm,NewsFeed,NewsCategory,NewsItem,?,?,?,?> fNews)
	{
		super(new NewsFactoryBuilder<>(IoLang.class,IoDescription.class,IoLocale.class,TenantRealm.class,NewsFeed.class,NewsCategory.class,NewsItem.class,IoMarkup.class,IoMarkupType.class),fNews);
	}
}