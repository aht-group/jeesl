package org.jeesl.interfaces.model.module.calendar.unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
@DownloadJeeslData
public interface JeeslCalendarWeekOfMonth <L extends JeeslLang, D extends JeeslDescription, S extends JeeslStatus<L,D,S>, G extends JeeslGraphic<?,?,?>>
					extends Serializable,EjbPersistable,
							EjbWithCode,JeeslStatusFixedCode,
							EjbWithCodeGraphic<G>,JeeslStatus<L,D,S>
{
	public static List<String> toFixedCodes()
	{
		List<String> fixed = new ArrayList<String>();
		for(int i=1;i<=6;i++){fixed.add(Integer.valueOf(i).toString());}
		return fixed;
	}
}