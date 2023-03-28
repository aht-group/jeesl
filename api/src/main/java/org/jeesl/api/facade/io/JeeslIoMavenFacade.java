package org.jeesl.api.facade.io;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface JeeslIoMavenFacade <L extends JeeslLang,D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact>
			extends JeeslFacade
{	
	ARTIFACT fIoMavenArtifact(GROUP group, String code) throws JeeslNotFoundException;
}