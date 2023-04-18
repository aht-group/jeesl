package org.jeesl.interfaces.model.io.mail.template;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoTemplateCategory <L extends JeeslLang, D extends JeeslDescription,S extends JeeslStatus<L,D,S>, G extends JeeslGraphic<?,?,?>>
					extends Serializable,EjbSaveable,
							EjbWithCodeGraphic<G>,JeeslStatus<L,D,S>
{	

}