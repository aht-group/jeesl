package org.jeesl.api.rest.i.module.news;

import org.jeesl.model.json.module.news.JsonNewsFeed;

public interface JeeslNewsRestInterface
{
	JsonNewsFeed feed(Long realm, Long feed, String localeCode);
}