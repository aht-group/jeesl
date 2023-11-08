package org.jeesl.interfaces.model.io.maven.dependency;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenMaintainer;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoMavenVersion <ARTIFACT extends JeeslIoMavenArtifact<?,?>,
										OUTDATE extends JeeslMavenOutdate<?,?,OUTDATE,?>,
										MAINTAINER extends JeeslMavenMaintainer<?,?,MAINTAINER,?>>
									extends EjbSaveable,//EjbRemoveable,
												EjbWithParentAttributeResolver,
												EjbWithNonUniqueCode,EjbWithPosition
{	
	public static enum Attributes{artifact,code,maintainer,label};
	
	String getLabel();
	void setLabel(String label);
	
	String getRemark();
	void setRemark(String remark);
	
	ARTIFACT getArtifact();
	void setArtifact(ARTIFACT artifact);
	
	OUTDATE getOutdate();
	void setOutdate(OUTDATE outdate);
	
	MAINTAINER getMaintainer();
	void setMaintainer(MAINTAINER maintainer);
}