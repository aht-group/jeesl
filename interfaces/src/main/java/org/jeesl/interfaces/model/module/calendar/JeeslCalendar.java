package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendar <ZONE extends JeeslCalendarZone<?,?>,
								CT extends JeeslCalendarScope<?,?,CT,?>
								>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					JeeslWithType<CT>
{
//	void test();
	
	ZONE getZone();
	void setZone(ZONE zone);
}