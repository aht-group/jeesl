package org.jeesl.interfaces.model.system.io.cms;

import java.io.Serializable;

import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.option.JeeslOptionRestDownload;

import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;

public interface JeeslIoCmsMarkupType <L extends JeeslLang, D extends JeeslDescription,
										S extends JeeslStatus<S,L,D>,
										G extends JeeslGraphic<L,D,?,?,?>>
					extends Serializable,EjbPersistable,
							JeeslOptionRestDownload,
							JeeslStatusFixedCode,
							JeeslStatus<S,L,D>,EjbWithCodeGraphic<G>	
{	
	public enum Code{text,xhtml}
}