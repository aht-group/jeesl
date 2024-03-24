package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenDependency;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslMavenScope;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeReferral;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoMavenFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
									GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,?,?>,
									DEPENDENCY extends JeeslIoMavenDependency<VERSION>,
									SCOPE extends JeeslMavenScope<?,?,SCOPE,?>,
									MODULE extends JeeslIoMavenModule<MODULE,?,?,?,?>,
									USAGE extends JeeslIoMavenUsage<VERSION,SCOPE,MODULE>,
									EER extends JeeslIoMavenEeReferral<VERSION,?,?>>
			extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(IoMavenFactoryBuilder.class);
	
	private final Class<GROUP> cGroup; public Class<GROUP> getClassGroup() {return cGroup;}
	private final Class<ARTIFACT> cArtifact; public Class<ARTIFACT> getClassArtifact() {return cArtifact;}
	private final Class<VERSION> cVersion; public Class<VERSION> getClassVersion() {return cVersion;}
	private final Class<DEPENDENCY> cDependency; public Class<DEPENDENCY> getClassDependency() {return cDependency;}
	private final Class<MODULE> cModule; public Class<MODULE> getClassModule() {return cModule;}
	private final Class<USAGE> cUsage; public Class<USAGE> getClassUsage() {return cUsage;}
	private final Class<EER> cEeReferral; public Class<EER> getClassEeReferral() {return cEeReferral;}
		
	public IoMavenFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<GROUP> cGroup,
									final Class<ARTIFACT> cArtifact,
									final Class<VERSION> cVersion,
									final Class<DEPENDENCY> cDependency,
									final Class<MODULE> cModule,
									final Class<USAGE> cUsage,
									final Class<EER> cEeReferral)
	{
		super(cL,cD);
		this.cGroup=cGroup;
		this.cArtifact=cArtifact;
		this.cVersion=cVersion;
		this.cDependency=cDependency;
		
		this.cModule=cModule;
		this.cUsage=cUsage;
		
		this.cEeReferral=cEeReferral;
	}
}