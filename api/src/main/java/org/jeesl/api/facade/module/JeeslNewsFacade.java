package org.jeesl.api.facade.module;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslNewsFacade <L extends JeeslLang, D extends JeeslDescription,
								R extends JeeslTenantRealm<L,D,R,?>,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,D,R,CATEGORY,USER,M>,
								USER extends EjbWithId,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>>
			extends JeeslFacade
{	
	List<ITEM> fNewsActive();
}