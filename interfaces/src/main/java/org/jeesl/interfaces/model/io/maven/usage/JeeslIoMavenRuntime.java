package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.with.EjbWithCodeGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatusFixedCode;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
@DownloadJeeslData
public interface JeeslIoMavenRuntime <L extends JeeslLang, D extends JeeslDescription,
						S extends JeeslStatus<L,D,S>,
						G extends JeeslGraphic<?,?,?>>
		extends Serializable,EjbPersistable,EjbWithSymbol,
				JeeslStatusFixedCode,
				EjbWithCodeGraphic<G>,JeeslStatus<L,D,S>
{
	public static enum Code{jsf,primefaces,hibernate};
}