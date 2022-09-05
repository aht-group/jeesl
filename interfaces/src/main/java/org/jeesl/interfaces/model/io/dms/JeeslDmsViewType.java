package org.jeesl.interfaces.model.io.dms;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

@DownloadJeeslData
public interface JeeslDmsViewType <S extends JeeslStatus<L,D,S>,
										L extends JeeslLang, D extends JeeslDescription,
										G extends JeeslGraphic<?,?,?>>
		extends Serializable,EjbPersistable,JeeslStatusFixedCode,EjbWithCodeGraphic<G>
{
	public enum Code{tree}
}