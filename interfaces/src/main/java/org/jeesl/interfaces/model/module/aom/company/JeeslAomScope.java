package org.jeesl.interfaces.model.module.aom.company;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;


@DownloadJeeslData
public interface JeeslAomScope <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslStatus<L,D,S>,
									G extends JeeslGraphic<?,?,?>>
					extends Serializable,EjbPersistable,
								EjbWithCode,JeeslStatusFixedCode,
								
								EjbWithCodeGraphic<G>,
								JeeslStatus<L,D,S>
{
	
	public static enum Code{manufacturer,vendor,maintainer,supply,insurance};
}