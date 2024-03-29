package org.jeesl.controller.web.c.module.news;

import org.jeesl.api.bean.callback.module.news.JeeslNewsRegistryCallback;
import org.jeesl.controller.web.g.module.news.JeeslNewsRegistryGwc;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.module.news.NewsCategory;
import org.jeesl.model.ejb.module.news.NewsFeed;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.jeesl.model.ejb.system.tenant.TenantRealm;

public class JeeslNewsRegistryWc <RREF extends EjbWithId,
									ITEM extends JeeslNewsItem<IoLang,NewsFeed,NewsCategory,SecurityUser,M,IoFileContainer>,
									M extends JeeslIoMarkup<MT>,
									MT extends JeeslIoMarkupType<IoLang,IoDescription,MT,?>
>
					extends JeeslNewsRegistryGwc<IoLang,IoDescription,IoLocale,TenantRealm,RREF,NewsFeed,NewsCategory,ITEM,SecurityUser,M,MT,IoFileContainer>
{
	private static final long serialVersionUID = 1L;

	public JeeslNewsRegistryWc(JeeslNewsRegistryCallback callback, NewsFactoryBuilder<IoLang,IoDescription,IoLocale,TenantRealm,NewsFeed,NewsCategory,ITEM,SecurityUser,M,MT> fbNews)
	{
		super(callback,fbNews);
	}
}