package org.jeesl.controller.handler.rest.module.news;

import java.time.LocalDate;
import java.util.ArrayList;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.rest.i.module.news.JeeslNewsRestInterface;
import org.jeesl.model.ejb.module.news.NewsFeed;
import org.jeesl.model.ejb.module.news.NewsItem;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.json.module.news.JsonNewsFeed;
import org.jeesl.model.json.module.news.JsonNewsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsRestHandler implements JeeslNewsRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(NewsRestHandler.class);

	private final JeeslFacade fNews;
	private NewsFeed feed; public NewsFeed getFeed() {return feed;} public void setFeed(NewsFeed feed) {this.feed = feed;}

	public NewsRestHandler(JeeslFacade fNews)
	{
		this.fNews=fNews;
	}
	
	@Override public JsonNewsFeed feed()
	{
		JsonNewsFeed json = new JsonNewsFeed();
		try
		{
			TenantRealm realm = fNews.find(TenantRealm.class,2370);
			TenantRealm rref = new TenantRealm(); rref.setId(17);
			
			NewsFeed feed = fNews.fByRref(NewsFeed.class,realm,rref);
			
			json.setItems(new ArrayList<>());
			
			for(NewsItem item : fNews.allForParent(NewsItem.class,feed))
			{
				json.getItems().add(item(item,"en"));
			}
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
	
		return json;
	}
	
	private JsonNewsItem item(NewsItem item, String localeCode)
	{
		JsonNewsItem json = new JsonNewsItem();
		json.setLocalDate(item.getRecord().toLocalDate());
		json.setHeadline(item.getName().get(localeCode).getLang());
		json.setMarkup(item.getMarkup().get(localeCode).getContent());
		
		return json;
	}
}