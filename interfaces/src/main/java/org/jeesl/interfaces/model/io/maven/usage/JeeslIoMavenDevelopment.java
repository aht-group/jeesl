package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenType;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenDevelopment <TYPE extends JeeslMavenType<?,?,TYPE,G>,
										G extends JeeslGraphic<?,?,?>>
								extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
												EjbWithCode,EjbWithGraphic<G>
{	
	
	public static enum Attributes{code};
	
	String getLabel();
	void setLabel(String label);
}