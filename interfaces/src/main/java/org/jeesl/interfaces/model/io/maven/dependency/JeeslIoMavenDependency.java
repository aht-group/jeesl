package org.jeesl.interfaces.model.io.maven.dependency;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenDependency <VERSION extends JeeslIoMavenVersion<?,?,?>>
									extends EjbSaveable,EjbRemoveable
{	
	public static enum Attributes{artifact,dependsOn};
	
	VERSION getArtifact();
	void setArtifact(VERSION artifact);
	
	VERSION getDependsOn();
	void setDependsOn(VERSION dependsOn);
}