package org.jeesl.interfaces.model.io.maven.dependency;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenSuitability;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenArtifact <GROUP extends JeeslIoMavenGroup,
										SUITABILITY extends JeeslMavenSuitability<?,?,SUITABILITY,?>>
									extends Serializable,EjbWithId,EjbRemoveable,EjbPersistable,EjbSaveable,
												EjbWithNonUniqueCode
{	
	
	public static enum Attributes{group,code};
	
	GROUP getGroup();
	void setGroup(GROUP group);
	
	SUITABILITY getSuitability();
	void setSuitability(SUITABILITY suitability);
}