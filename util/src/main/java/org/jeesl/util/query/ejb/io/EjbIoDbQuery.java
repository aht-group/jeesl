package org.jeesl.util.query.ejb.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoDbQuery;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoDbQuery<SYSTEM extends JeeslIoSsiSystem<?,?>,
						SNAP extends JeeslDbMetaSnapshot<SYSTEM,?,?,?,?>
>
			extends AbstractEjbQuery
			implements JeeslIoDbQuery<SYSTEM,SNAP>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbIoDbQuery.class);
	
	public EjbIoDbQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		super.reset();
		systems=null;
		snapshots=null;
	}
	
	//Pagination
	public EjbIoDbQuery<SYSTEM,SNAP> maxResults(Integer max) {super.setMaxResults(max); return this;}
	
	//Fetches
	public <E extends Enum<E>> EjbIoDbQuery<SYSTEM,SNAP> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//JEESL-CQ
	public EjbIoDbQuery<SYSTEM,SNAP> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbIoDbQuery<SYSTEM,SNAP> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoDbQuery<SYSTEM,SNAP> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoDbQuery<SYSTEM,SNAP> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoDbQuery<SYSTEM,SNAP> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoDbQuery<SYSTEM,SNAP> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<SYSTEM> systems; @Override public List<SYSTEM> getSystems() {return systems;}
	public EjbIoDbQuery<SYSTEM,SNAP> add(SYSTEM system) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.add(system); return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> addSystems(List<SYSTEM> list) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.addAll(list); return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> addSystems(Collection<SYSTEM> collection) {if(Objects.isNull(systems)) {systems = new ArrayList<>();} systems.addAll(collection); return this;}
	
	private List<SNAP> snapshots; @Override public List<SNAP> getSnapshots() {return snapshots;}
	public EjbIoDbQuery<SYSTEM,SNAP> add(SNAP snapshot) {if(Objects.isNull(snapshots)) {snapshots = new ArrayList<>();} snapshots.add(snapshot); return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> addSnapshots(List<SNAP> list) {if(Objects.isNull(snapshots)) {snapshots = new ArrayList<>();} snapshots.addAll(list); return this;}
	public EjbIoDbQuery<SYSTEM,SNAP> addSnapshots(Collection<SNAP> collection) {if(Objects.isNull(snapshots)) {snapshots = new ArrayList<>();} snapshots.addAll(collection); return this;}
}