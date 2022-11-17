package org.jeesl.interfaces.model.system.locale;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslLocale <L extends JeeslLang, D extends JeeslDescription,
								S extends JeeslStatus<L,D,S>,
								G extends JeeslGraphic<?,?,?>>
						extends Serializable,EjbPersistable,
								JeeslStatusFixedCode,EjbWithCodeGraphic<G>,
								JeeslStatus<L,D,S>
{
	public static String none = "none";
	public enum Attributes{name}
}