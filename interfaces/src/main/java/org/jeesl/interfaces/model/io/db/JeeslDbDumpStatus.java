package org.jeesl.interfaces.model.io.db;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDescription;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;

public interface JeeslDbDumpStatus <L extends JeeslLang, D extends JeeslDescription,
										S extends JeeslStatus<S,L,D>,
										G extends JeeslGraphic<L,D,?,?,?>>
							extends Serializable,EjbPersistable,
									JeeslOptionRestDescription,JeeslOptionRestDownload,
									EjbWithCodeGraphic<G>,JeeslStatusFixedCode,
									JeeslStatus<S,L,D>
{	
	public static enum Code{stored,flagged,deleted};
}