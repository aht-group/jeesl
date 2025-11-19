package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenArtifact;
import org.jeesl.interfaces.model.io.maven.dependency.JeeslIoMavenVersion;
import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;

public interface JeeslIoMavenQuery <
							ARTIFACT extends JeeslIoMavenArtifact<?,?>,
							VERSION extends JeeslIoMavenVersion<ARTIFACT,?,?>,
							MODULE extends JeeslIoMavenModule<MODULE,STRUCTURE,TYPE,?,?>,
							STRUCTURE extends JeeslMavenStructure<?,?,STRUCTURE,?>,
							TYPE extends JeeslMavenType<?,?,TYPE,?>
							>
			extends JeeslCoreQuery,JeeslOrderingQuery
{
	boolean isDistinct();
//	void x();
	
	//Pagination
	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> maxResults(Integer max);
	
	//Fetches
	List<String> getRootFetches();
	public <E extends Enum<E>> JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> addRootFetch(E e);
	
	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> add(JeeslCqLiteral literal);
	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> add(JeeslCqBoolean bool);
	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> orderBy(JeeslCqOrdering ordering);
	
	//LocalDate
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
//	public EjbIoMavenQuery<L,D,GROUP,ARTIFACT,VERSION,OUTDATE,MAINTAINER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	List<Long> getIdList();
	
	List<ARTIFACT> getIoMavenArtifacts();
	
	List<VERSION> getIoMavenVersions();
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> add(VERSION version);
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> addVersions(Collection<VERSION> list);
	
	List<MODULE> getIoMavenModules();
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> add(MODULE module);
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> addModules(List<MODULE> list);
	
	List<STRUCTURE> getIoMavenStructures();
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> add(STRUCTURE structure);
//	JeeslIoMavenQuery<ARTIFACT,VERSION,MODULE,STRUCTURE,TYPE> addStructures(List<STRUCTURE> list);
	
	List<TYPE> getIoMavenTypes();
	
}