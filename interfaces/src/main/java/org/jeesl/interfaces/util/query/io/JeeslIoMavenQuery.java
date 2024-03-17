package org.jeesl.interfaces.util.query.io;

import java.util.Collection;
import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;

public interface JeeslIoMavenQuery <
							VERSION extends JeeslIoMavenVersion<?,?,?>,
							MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,?,?,?>,
							STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>
							>
			extends JeeslCoreQuery
{
	boolean isDistinct();
//	void x();
	
	//Fetches
	List<String> getRootFetches();
	public <E extends Enum<E>> JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> addRootFetch(E e);
	
	//LocalDate
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	List<Long> getIdList();
	
	List<VERSION> getVersions();
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> add(VERSION version);
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> addVersions(Collection<VERSION> list);
	
	List<MODULE> getModules();
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> add(MODULE module);
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> addModules(List<MODULE> list);
	
	List<STRUCTURE> getStructures();
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> add(STRUCTURE structure);
	JeeslIoMavenQuery<VERSION,MODULE,STRUCTURE> addStructures(List<STRUCTURE> list);
	
}