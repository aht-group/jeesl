package org.jeesl.interfaces.model.module.calendar;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendarZone <L extends JeeslLang, D extends JeeslDescription>
						extends Serializable,EjbSaveable,EjbRemoveable,EjbWithCode,EjbWithLang<L>
{
	public enum Attributes{calendar}
	
	public static String tzUtc = "UTC";
	public static String tzBerlin = "Europe/Berlin";
}