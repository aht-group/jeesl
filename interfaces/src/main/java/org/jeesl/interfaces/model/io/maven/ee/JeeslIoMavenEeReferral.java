package org.jeesl.interfaces.model.io.maven.ee;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;

@DownloadJeeslData
public interface JeeslIoMavenEeReferral <EDITION extends JeeslIoMavenEeEdition<?,?,EDITION,?>,
										STANDARD extends JeeslIoMavenEeStandard<?,?,STANDARD,?>>
					extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithPosition
{	
	public enum Attributes{edition,standard,recommendation};
	
	Boolean getRecommendation();
	void setRecommendation(Boolean recommendation);
}