package org.jeesl.controller.facade.module;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.module.news.JeeslWithNewsFeed;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslNewsFacadeBean<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
								R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
								FEED extends JeeslNewsFeed<L,D,R>,
								CAT extends JeeslNewsCategory<L,D,R,CAT,?>,
								ITEM extends JeeslNewsItem<L,FEED,CAT,USER,M,FRC>,
								USER extends EjbWithId,
								M extends JeeslMarkup<MT>,
								MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
								FRC extends JeeslFileContainer<?,?>>
					extends JeeslFacadeBean
					implements JeeslNewsFacade<L,D,R,FEED,CAT,ITEM,USER,M,MT,FRC>
{	
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslNewsFacadeBean.class);
	
	private final NewsFactoryBuilder<L,D,LOC,R,FEED,CAT,ITEM,USER,M,MT> fbNews;
	
	public JeeslNewsFacadeBean(EntityManager em, NewsFactoryBuilder<L,D,LOC,R,FEED,CAT,ITEM,USER,M,MT> fbNews)
	{
		super(em);
		this.fbNews=fbNews;
	}

	@Override
	public <OWNER extends JeeslWithNewsFeed<FEED>> FEED fNewsFeed(Class<OWNER> cOwner, OWNER owner) throws JeeslNotFoundException
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<FEED> cQ = cB.createQuery(fbNews.getClassFeed());
		Root<OWNER> root = cQ.from(cOwner);
		
		Path<FEED> pathCalendar = root.get(JeeslWithNewsFeed.Attributes.newsFeed.toString());
		Path<Long> pId = root.get(EjbWithId.attribute);
		
		cQ.where(cB.equal(pId,owner.getId()));
		cQ.select(pathCalendar);
		
		try	{return em.createQuery(cQ).getSingleResult();}
		catch (NoResultException ex){throw new JeeslNotFoundException("No "+fbNews.getClassFeed()+" found for owner "+owner);}
		catch (NonUniqueResultException ex){throw new JeeslNotFoundException("Multiple "+fbNews.getClassFeed()+" found for owner "+owner);}
	}

	@Override public List<ITEM> fNewsActive()
	{
		return this.all(fbNews.getClassItem());
	}
}