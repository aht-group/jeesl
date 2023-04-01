package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenDevelopment;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoMavenFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,?,?>,
									DEVELOPMENT extends JeeslIoMavenDevelopment>
			extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(IoMavenFactoryBuilder.class);
	
	private final Class<GROUP> cGroup; public Class<GROUP> getClassGroup() {return cGroup;}
	private final Class<ARTIFACT> cArtifact; public Class<ARTIFACT> getClassArtifact() {return cArtifact;}
	private final Class<VERSION> cVersion; public Class<VERSION> getClassVersion() {return cVersion;}
		
	public IoMavenFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<GROUP> cGroup,
									final Class<ARTIFACT> cArtifact,
									final Class<VERSION> cVersion,
									final Class<DEVELOPMENT> cDevelopment)
	{
		super(cL,cD);
		this.cGroup=cGroup;
		this.cArtifact=cArtifact;
		this.cVersion=cVersion;
	}
}