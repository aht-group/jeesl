package org.jeesl.interfaces.model.io.cms;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslIoCmsElementCategory <L extends JeeslLang, D extends JeeslDescription,
										S extends JeeslStatus<L,D,S>,
										G extends JeeslGraphic<L,D,?,?,?>>
					extends Serializable,EjbPersistable,JeeslStatusFixedCode,
							JeeslStatus<L,D,S>,EjbWithCodeGraphic<G>
{	

}