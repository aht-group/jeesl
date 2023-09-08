package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenMaintainer;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.util.query.io.EjbIoMavenQuery;

public interface JeeslIoMavenFacade <GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,OUTDATE,MAINTAINER>,
									OUTDATE extends JeeslMavenOutdate<?,?,OUTDATE,?>,
									MAINTAINER extends JeeslMavenMaintainer<?,?,MAINTAINER,?>,
									STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>,
									USAGE extends JeeslIoMavenUsage<VERSION,?>>
			extends JeeslFacade
{	
	ARTIFACT fIoMavenArtifact(GROUP group, String code) throws JeeslNotFoundException;
	VERSION fIoMavenVersion(ARTIFACT artifact, String code) throws JeeslNotFoundException;
	
	List<USAGE> fIoMavenUsages(EjbIoMavenQuery<GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER,STRUCTURE> query);
}