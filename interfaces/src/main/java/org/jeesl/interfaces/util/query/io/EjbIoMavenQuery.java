package org.jeesl.interfaces.util.query.io;

import java.util.Collection;
import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;

public interface EjbIoMavenQuery <
							VERSION extends JeeslIoMavenVersion<?,?,?>,
							MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,?,?,?>,
							STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>
							>
//			extends AbstractEjbQuery
{
	boolean isDistinct();
//	void x();
	
	//Fetches
	List<String> getRootFetches();
	public <E extends Enum<E>> EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> addRootFetch(E e);
	
	//LocalDate
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	List<Long> getIdList();
	
	List<VERSION> getVersions();
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> add(VERSION version);
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> addVersions(Collection<VERSION> list);
	
	List<MODULE> getModules();
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> add(MODULE module);
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> addModules(List<MODULE> list);
	
	List<STRUCTURE> getStructures();
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> add(STRUCTURE structure);
	EjbIoMavenQuery<VERSION,MODULE,STRUCTURE> addStructures(List<STRUCTURE> list);
	
}