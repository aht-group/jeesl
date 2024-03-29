package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenMaintainer;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenDependency;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslMavenScope;
import org.jeesl.interfaces.model.io.maven.ee.JeeslIoMavenEeReferral;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.util.query.io.JeeslIoMavenQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;

public interface JeeslIoMavenFacade <GROUP extends JeeslIoMavenGroup,
									ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
									VERSION extends JeeslIoMavenVersion<ARTIFACT,OUTDATE,MAINTAINER>,
									DEPENDENCY extends JeeslIoMavenDependency<VERSION>,
									SCOPE extends JeeslMavenScope<?,?,SCOPE,?>,
									OUTDATE extends JeeslMavenOutdate<?,?,OUTDATE,?>,
									MAINTAINER extends JeeslMavenMaintainer<?,?,MAINTAINER,?>,
									MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,TYPE,?,?>,
									STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>,
									TYPE extends JeeslMavenType<?,?,TYPE,?>,
									USAGE extends JeeslIoMavenUsage<VERSION,SCOPE,MODULE>,
									EEF extends JeeslIoMavenEeReferral<VERSION,?,?>>
			extends JeeslFacade
{	
	ARTIFACT fIoMavenArtifact(GROUP group, String code) throws JeeslNotFoundException;
	VERSION fIoMavenVersion(ARTIFACT artifact, String code) throws JeeslNotFoundException;
	
	List<MODULE> fIoMavenModules(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	List<USAGE> fIoMavenUsages(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	List<DEPENDENCY> fIoMavenDependencies(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	List<ARTIFACT> fIoMavenArtifacts(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	List<VERSION> fIoMavenVersions(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	List<EEF> fIoMavenEeReferrals(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	
	Long cIoMavenVersions(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
	
	JsonTuples1<VERSION> tpUsageByVersion(JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> query);
}