package org.jeesl.interfaces.model.module.news;

import java.time.LocalDateTime;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.io.cms.JeeslWithMarkupMulti;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslNewsItem<L extends JeeslLang,
								FEED extends JeeslNewsFeed<L,?,?>,
								CATEGORY extends JeeslNewsCategory<L,?,?,CATEGORY,?>,
								USER extends JeeslSimpleUser,
								M extends JeeslMarkup<?>,
								FRC extends JeeslFileContainer<?,?>>
							extends EjbWithId,EjbSaveable,EjbRemoveable,EjbWithVisible,
									EjbWithParentAttributeResolver,
									EjbWithLang<L>,JeeslWithMarkupMulti<M>,
									JeeslWithFileRepositoryContainer<FRC>
{
	public static enum Attributes{feed,visible,validFrom}
	
	FEED getFeed();
	void setFeed(FEED feed);
	
	CATEGORY getCategory();
	void setCategory(CATEGORY category);
	
	USER getAuthor();
	void setAuthor(USER user);
	
	LocalDateTime getRecord();
	void setRecord(LocalDateTime record);
	
	LocalDateTime getValidFrom();
	void setValidFrom(LocalDateTime validFrom);
	
	LocalDateTime getValidUntil();
	void setValidUntil(LocalDateTime validUntil);
}