package org.jeesl.interfaces.model.module.news;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithMarkup;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslNewsItem<L extends JeeslLang,
								FEED extends JeeslNewsFeed<L,?,?>,
								CATEGORY extends JeeslNewsCategory<L,?,?,CATEGORY,?>,
								USER extends EjbWithId,
								M extends JeeslMarkup<?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithVisible,
				EjbWithLang<L>,EjbWithMarkup<M>
{
	public static enum Attributes{visible,validFrom}
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	USER getAuthor();
	void setAuthor(USER user);
	
	LocalDateTime getValidFrom();
	void setValidFrom(LocalDateTime validFrom);
}