package org.jeesl.interfaces.model.io.revision.entity;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusWithSymbol;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.option.JeeslRestDownloadOption;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;

public interface JeeslRevisionEntityRelation <L extends JeeslLang, D extends JeeslDescription,
												S extends JeeslStatus<L,D,S>,
												G extends JeeslGraphic<L,D,?,?,?>>
		extends Serializable,EjbPersistable,JeeslStatusWithSymbol,
					JeeslRestDownloadOption,
					EjbWithCodeGraphic<G>,
					JeeslStatusFixedCode,JeeslStatus<L,D,S>
{	
	public enum Code{MtoO,OtoO,OtoM,MtoM}
}