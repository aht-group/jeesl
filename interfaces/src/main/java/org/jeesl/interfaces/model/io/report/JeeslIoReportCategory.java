package org.jeesl.interfaces.model.io.report;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoReportCategory <L extends JeeslLang, D extends JeeslDescription,
										S extends JeeslStatus<L,D,S>,
										G extends JeeslGraphic<?,?,?>>
		extends Serializable,EjbPersistable,
				EjbWithCodeGraphic<G>,JeeslStatus<L,D,S>
{
	public enum Attributes{position}
}