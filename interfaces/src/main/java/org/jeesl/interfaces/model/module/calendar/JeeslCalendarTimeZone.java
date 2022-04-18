package org.jeesl.interfaces.model.module.calendar;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslCalendarTimeZone <L extends JeeslLang, D extends JeeslDescription>
		extends EjbWithId,EjbSaveable,EjbWithCode,EjbWithLang<L>
{
	public static String tzUtc = "UTC";
	public static String tzBerlin = "Europe/Berlin";
}