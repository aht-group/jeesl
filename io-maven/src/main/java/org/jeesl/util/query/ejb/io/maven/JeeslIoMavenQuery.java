package org.jeesl.util.query.ejb.io.maven;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.jeesl.interfaces.util.query.io.EjbIoMavenQuery;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenQuery extends AbstractEjbQuery implements EjbIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure>
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
	public <E extends Enum<E>> JeeslIoMavenQuery addRootFetch(E e) {if(Objects.isNull(rootFetches)) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public JeeslIoMavenQuery distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public JeeslIoMavenQuery id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> JeeslIoMavenQuery ids(List<T> ids) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(EjbIdFactory.toIds(ids)); return this;}
	@Override public JeeslIoMavenQuery idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public JeeslIoMavenQuery codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public JeeslIoMavenQuery ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public JeeslIoMavenQuery ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public JeeslIoMavenQuery ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	private List<IoMavenVersion> versions;
	public List<IoMavenVersion> getVersions() {return versions;}
	public JeeslIoMavenQuery add(IoMavenVersion version) {if(Objects.isNull(versions)) {versions = new ArrayList<>();} versions.add(version); return this;}
	public JeeslIoMavenQuery addVersions(Collection<IoMavenVersion> list) {if(Objects.isNull(versions)) {versions = new ArrayList<>();} versions.addAll(list); return this;}

	private List<IoMavenStructure> structures;
	@Override public List<IoMavenStructure> getStructures() {return structures;}
	@Override public EjbIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure> add(IoMavenStructure structure) {if(Objects.isNull(structures)) {structures = new ArrayList<>();} structures.add(structure); return this;}
	@Override public EjbIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure> addStructures(List<IoMavenStructure> list) {if(Objects.isNull(structures)) {structures = new ArrayList<>();} structures.addAll(list); return this;}
	
	private List<IoMavenModule> modules;
	@Override public List<IoMavenModule> getModules() {return modules;}
	@Override public EjbIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure> add(IoMavenModule module) {if(Objects.isNull(modules)) {modules = new ArrayList<>();} modules.add(module); return this;}
	@Override public EjbIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure> addModules(List<IoMavenModule> list) {if(Objects.isNull(modules)) {modules = new ArrayList<>();} modules.addAll(list); return this;}
}