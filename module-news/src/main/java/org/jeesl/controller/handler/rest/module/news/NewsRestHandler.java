package org.jeesl.controller.handler.rest.module.news;

import java.time.LocalDate;
import java.util.ArrayList;

import org.jeesl.interfaces.rest.i.module.news.JeeslNewsRestInterface;
import org.jeesl.model.json.module.news.JsonNewsFeed;
import org.jeesl.model.json.module.news.JsonNewsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewsRestHandler implements JeeslNewsRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(NewsRestHandler.class);
		
	public NewsRestHandler()
	{
		
	}
	
	@Override public JsonNewsFeed feed()
	{
		JsonNewsFeed json = new JsonNewsFeed();
		json.setItems(new ArrayList<>());
		
		json.getItems().add(item());
		json.getItems().add(item());
		
		return json;
	}
	
	private JsonNewsItem item()
	{
		JsonNewsItem json = new JsonNewsItem();
		json.setLocalDate(LocalDate.now());
		json.setHeadline("bla blab");
		
		return json;
	}
}