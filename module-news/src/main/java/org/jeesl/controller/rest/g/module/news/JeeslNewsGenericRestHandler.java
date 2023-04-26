package org.jeesl.controller.rest.g.module.news;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.facade.module.JeeslNewsFacade;
import org.jeesl.api.rest.i.module.news.JeeslNewsRestInterface;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.NewsFactoryBuilder;
import org.jeesl.interfaces.model.module.news.JeeslNewsCategory;
import org.jeesl.interfaces.model.module.news.JeeslNewsFeed;
import org.jeesl.interfaces.model.module.news.JeeslNewsItem;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.ejb.module.news.NewsFeed;
import org.jeesl.model.json.module.news.JsonNewsFeed;
import org.jeesl.model.json.module.news.JsonNewsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslNewsGenericRestHandler <R extends JeeslTenantRealm<?,?,R,?>,
						FEED extends JeeslNewsFeed<?,?,R>,
						CATEGORY extends JeeslNewsCategory<?,?,R,CATEGORY,?>,
						ITEM extends JeeslNewsItem<?,FEED,CATEGORY,?,?,?>>
				implements JeeslNewsRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(JeeslNewsGenericRestHandler.class);

	private final JeeslNewsFacade<?,?,R,FEED,CATEGORY,ITEM,?,?,?,?> fNews;
	private final NewsFactoryBuilder<?,?,?,R,FEED,CATEGORY,ITEM,?,?,?> fbNews;
	
	private NewsFeed feed; public NewsFeed getFeed() {return feed;} public void setFeed(NewsFeed feed) {this.feed = feed;}

	public JeeslNewsGenericRestHandler(NewsFactoryBuilder<?,?,?,R,FEED,CATEGORY,ITEM,?,?,?> fbNews, JeeslNewsFacade<?,?,R,FEED,CATEGORY,ITEM,?,?,?,?> fNews)
	{
		this.fbNews=fbNews;
		this.fNews=fNews;
	}
	
	@Override public JsonNewsFeed feed(Long realmId, Long feedId, String localeCode)
	{
		if (localeCode == null || localeCode.length() < 2) { localeCode = "en"; }
		localeCode = localeCode.substring(0, 2);
		
		JsonNewsFeed json = new JsonNewsFeed();
		try
		{
			R realm = fNews.find(fbNews.getClassRealm(),2370);
			FEED rref = fbNews.ejbFeed().build(realm,null); rref.setId(17);
			
			FEED feed = fNews.fByRref(fbNews.getClassFeed(),realm,rref);
			json.setItems(new ArrayList<>());
			
			List<ITEM> items = fNews.fNewsActiveItems(feed);
			logger.info(feed.toString()+" "+fbNews.getClassItem().getSimpleName()+": "+items.size());
			
			for(ITEM item : items)
			{
				json.getItems().add(item(item,localeCode));
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
	
		return json;
	}
	
	private JsonNewsItem item(ITEM item, String localeCode)
	{
		JsonNewsItem json = new JsonNewsItem();
		json.setLocalDate(item.getRecord().toLocalDate());
		json.setHeadline(item.getName().get(localeCode).getLang());
		json.setMarkup(item.getMarkup().get(localeCode).getContent());
		
		return json;
	}
}