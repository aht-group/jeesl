package org.jeesl.factory.ejb.module.news;

import java.time.LocalDateTime;
import java.util.List;

import org.jeesl.factory.ejb.system.locale.EjbMarkupFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbNewsItemFactory<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
									R extends JeeslTenantRealm<L,D,R,?>,
									FEED extends JeeslNewsFeed<L,D,R>,
									CATEGORY extends JeeslNewsCategory<L,D,R,CATEGORY,?>,
									ITEM extends JeeslNewsItem<L,FEED,CATEGORY,USER,M,?>,
									USER extends EjbWithId,
									M extends JeeslMarkup<MT>,
									MT extends JeeslIoCmsMarkupType<L,D,MT,?>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbNewsItemFactory.class);
	
	final Class<ITEM> cItem;
	
	private final EjbLangFactory<L> efLang;
	private final EjbMarkupFactory<LOC,M,MT> efMarkup;
	
    public EjbNewsItemFactory(Class<L> cLang, Class<ITEM> cItem, Class<M> cMarkup)
    {
        this.cItem = cItem;
        efLang = EjbLangFactory.instance(cLang);
        efMarkup = EjbMarkupFactory.instance(cMarkup);
    }
	
	public <RREF extends EjbWithId> ITEM build(R realm, RREF rref, List<LOC> locales, MT markupType, USER author)
	{
		try
		{
			ITEM ejb = cItem.newInstance();
			ejb.setName(efLang.buildEmpty(locales));
			ejb.setMarkup(efMarkup.build(locales,markupType));
			ejb.setAuthor(author);
			ejb.setValidFrom(LocalDateTime.now());
	
		    return ejb;
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return null;
    }
	
	public void converter(JeeslFacade facade, ITEM item)
	{
//		if(event.getCategory()!=null) {event.setCategory(facade.find(fbHd.getClassCategory(),event.getCategory()));}
//		if(event.getStatus()!=null) {event.setStatus(facade.find(fbHd.getClassTicketStatus(),event.getStatus()));}
//		if(event.getLevel()!=null) {event.setLevel(facade.find(fbHd.getClassLevel(),event.getLevel()));}
//		if(event.getSupporter()!=null) {event.setSupporter(facade.find(fbHd.getClassUser(),event.getSupporter()));}
//		if(event.getReporterPriority()!=null) {event.setReporterPriority(facade.find(fbHd.getClassPriority(),event.getReporterPriority()));}
//		if(event.getSupporterPriority()!=null) {event.setSupporterPriority(facade.find(fbHd.getClassPriority(),event.getSupporterPriority()));}
	}
}