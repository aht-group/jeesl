package org.jeesl.interfaces.model.module.calendar.sub;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithCategory;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeesCalSubscription <CAT extends JeeslCalendarSubscriptionCategory<?,?,CAT,?>>
					extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
							EjbWithCode,JeeslWithCategory<CAT>
						
{
	public static enum Attributes{id};
	
	
}