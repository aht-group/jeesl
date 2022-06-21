package org.jeesl.controller.facade.module;

import java.util.List;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslNewsFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
								ITEM extends JeeslNewsItem<L,D,R,CATEGORY,USER>,
								USER extends EjbWithId>
					extends JeeslFacadeBean
					implements JeeslNewsFacade<L,D,R,CATEGORY,ITEM,USER>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslNewsFacadeBean.class);
	
	private final NewsFactoryBuilder<L,D,LOC,R,CATEGORY,ITEM,USER> fbNews;
	
	public JeeslNewsFacadeBean(EntityManager em, NewsFactoryBuilder<L,D,LOC,R,CATEGORY,ITEM,USER> fbNews)
	{
		super(em);
		this.fbNews=fbNews;
	}

	@Override
	public List<ITEM> fNewsActive()
	{
		return this.all(fbNews.getClassItem());
	}
}