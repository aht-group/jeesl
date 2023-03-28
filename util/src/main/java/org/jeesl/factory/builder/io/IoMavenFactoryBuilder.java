package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoMavenFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoMavenFactoryBuilder.class);
	
	private final Class<GROUP> cGroup; public Class<GROUP> getClassGroup() {return cGroup;}
	private final Class<ARTIFACT> cArtifact; public Class<ARTIFACT> getClassArtifact() {return cArtifact;}
		
	public IoMavenFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<GROUP> cGroup,
									final Class<ARTIFACT> cArtifact)
	{
		super(cL,cD);
		this.cGroup=cGroup;
		this.cArtifact=cArtifact;
	}
	
	
}