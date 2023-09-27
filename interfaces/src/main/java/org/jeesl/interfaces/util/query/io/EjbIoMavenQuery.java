package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenMaintainer;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenGroup;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;

public interface EjbIoMavenQuery <GROUP extends JeeslIoMavenGroup,
							ARTIFACT extends JeeslIoMavenArtifact<GROUP,?>,
							VERSION extends JeeslIoMavenVersion<ARTIFACT,OUTDATE,MAINTAINER>,
							OUTDATE extends JeeslMavenOutdate<?,?,OUTDATE,?>,
							MAINTAINER extends JeeslMavenMaintainer<?,?,MAINTAINER,?>,
							STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>>
//			extends AbstractEjbQuery
{
	boolean isDistinct();
	
	List<String> getRootFetches();
	//Fetches
//	public <E extends Enum<E>> EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	
	//LocalDate
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	List<Long> getIdList();
	
	List<VERSION> getVersions();
	EjbIoMavenQuery<GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER,STRUCTURE> add(VERSION version);
	EjbIoMavenQuery<GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER,STRUCTURE> addVersions(List<VERSION> list);
	
	List<STRUCTURE> getStructures();
	EjbIoMavenQuery<GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER,STRUCTURE> add(STRUCTURE structure);
	EjbIoMavenQuery<GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER,STRUCTURE> addStructures(List<STRUCTURE> list);
}