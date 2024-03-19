package org.jeesl.interfaces.model.io.maven.ee;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslData;

@DownloadJeeslData
public interface JeeslMavenEeReferral <EDITION extends JeeslMavenEeEdition<?,?,EDITION,?>,
										STANDARD extends JeeslMavenEeStandard<?,?,STANDARD,?>>
					extends Serializable,EjbSaveable,
								EjbWithPosition
{	
	public enum Attributes{edition,standard};
	
	Boolean getRecommendation();
	void setRecommendation(Boolean recommendation);
}