package org.jeesl.interfaces.model.module.journal;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;

public interface JeeslJournalScope <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>,G extends JeeslGraphic<?,?,?>>
									extends Serializable,EjbPersistable,
									EjbWithCode,
//									JeeslStatusFixedCode,
//									JeeslOptionRestDownload,
									EjbWithCodeGraphic<G>,
									JeeslStatus<L,D,S>
{
//	public static enum Code{direct,moderator,closed};
}