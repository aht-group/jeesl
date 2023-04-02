package org.jeesl.util.query.ejb.io.maven;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.jeesl.interfaces.util.query.io.EjbIoMavenQuery;
import org.jeesl.model.ejb.io.maven.classification.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.classification.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.classification.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenQuery 
			extends AbstractEjbQuery implements EjbIoMavenQuery<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenQuery.class);
	
	private JeeslIoMavenQuery()
	{       
		reset();
	}
	
	public static JeeslIoMavenQuery instance() {return new JeeslIoMavenQuery();}

	@Override public void reset()
	{
		
	}
	
	//Fetches
	public <E extends Enum<E>> JeeslIoMavenQuery addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	
	//LocalDate
	public JeeslIoMavenQuery ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public JeeslIoMavenQuery ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public JeeslIoMavenQuery ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	private List<IoMavenVersion> versions;
	public List<IoMavenVersion> getVersions() {return versions;}
	public JeeslIoMavenQuery add(IoMavenVersion version) {if(Objects.isNull(versions)) {versions = new ArrayList<>();} versions.add(version); return this;}
	public JeeslIoMavenQuery addVersions(List<IoMavenVersion> list) {if(Objects.isNull(versions)) {versions = new ArrayList<>();} versions.addAll(list); return this;}

	private List<IoMavenStructure> structures;
	@Override public List<IoMavenStructure> getStructures() {return structures;}
	@Override public EjbIoMavenQuery<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure> add(IoMavenStructure structure) {if(Objects.isNull(structures)) {structures = new ArrayList<>();} structures.add(structure); return this;}
	@Override public EjbIoMavenQuery<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure> addStructures(List<IoMavenStructure> list) {if(Objects.isNull(structures)) {structures = new ArrayList<>();} structures.addAll(list); return this;}
}