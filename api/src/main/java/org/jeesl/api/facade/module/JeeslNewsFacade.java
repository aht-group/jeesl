package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.module.news.JeeslWithNewsFeed;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslNewsFacade <L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								FEED extends JeeslNewsFeed<L,D,R>,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,FEED,CATEGORY,USER,M,FRC>,
								USER extends JeeslSimpleUser,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
								FRC extends JeeslFileContainer<?,?>>
			extends JeeslFacade
{	
	<OWNER extends JeeslWithNewsFeed<FEED>> FEED fNewsFeed(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException;
	List<ITEM> fNewsActiveItems(FEED feed);
}