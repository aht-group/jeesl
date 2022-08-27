package org.jeesl.interfaces.model.module.news;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWithNewsFeed <FEED extends JeeslNewsFeed<?,?,?>> extends EjbWithId
{
	public enum Attributes{newsFeed}
	
	FEED getNewsFeed();
	void setNewsFeed(FEED newsFeed);
}