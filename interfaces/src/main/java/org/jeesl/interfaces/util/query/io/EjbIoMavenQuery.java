package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslMavenScope;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;

public interface EjbIoMavenQuery <
							VERSION extends JeeslIoMavenVersion<?,?,?>,
							SCOPE extends JeeslMavenScope<?,?,SCOPE,?>,
							MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,?,?,?>,
							STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>>
//			extends AbstractEjbQuery
{
	boolean isDistinct();
	
	//Fetches
	List<String> getRootFetches();
	public <E extends Enum<E>> EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> addRootFetch(E e);
	
	//LocalDate
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	List<Long> getIdList();
	
	List<VERSION> getVersions();
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> add(VERSION version);
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> addVersions(List<VERSION> list);
	
	List<STRUCTURE> getStructures();
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> add(STRUCTURE structure);
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> addStructures(List<STRUCTURE> list);
	
	List<MODULE> getModules();
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> add(MODULE module);
	EjbIoMavenQuery<VERSION,SCOPE,MODULE,STRUCTURE> addModules(List<MODULE> list);
}