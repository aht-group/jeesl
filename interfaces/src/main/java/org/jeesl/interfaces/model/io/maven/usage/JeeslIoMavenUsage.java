package org.jeesl.interfaces.model.io.maven.usage;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenUsage <VERSION extends JeeslIoMavenVersion<?>,
									DEVELOPMENT extends JeeslIoMavenDevelopment>
									extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable
{
	public static enum Attributes{version};
	
	VERSION getVersion();
	void setVersion(VERSION version);
	
	DEVELOPMENT getDevelopment();
	void setDevelopment(DEVELOPMENT group);
}