package org.jeesl.interfaces.model.io.ai;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithTimeRange;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
@DownloadJeeslData
public interface JeeslIoAiOpenAiCompletion  <T extends JeeslIoAiOpenAiThread<USER>,
											M extends JeeslIoMarkup<?>,
											USER extends JeeslSimpleUser>
					extends Serializable,EjbSaveable,
							EjbWithParentAttributeResolver,
								JeeslWithTimeRange
{	
	public static enum Attributes{thread};
	
	T getThread();
	void setThread(T thread);
	
	USER getAuthor();
	void setAuthor(USER user);
	
	M getMarkupUser();
	void setMarkupUser(M markupUser);
	
	M getMarkupCompletion();
	void setMarkupCompletion(M markupUser);
}