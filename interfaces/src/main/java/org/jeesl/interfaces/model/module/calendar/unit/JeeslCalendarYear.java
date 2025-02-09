package org.jeesl.interfaces.model.module.calendar.unit;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslCalendarYear <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, G extends JeeslGraphic<?,?,?>>
					extends EjbPersistable, EjbWithCode,
							EjbWithCodeGraphic<G>,JeeslStatus<L,D,S>
{
	public enum Att {position}
}