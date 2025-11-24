package org.jeesl.util.query.ejb.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoSsiQuery;
import org.jeesl.util.query.cq.CqLong;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.jeesl.util.query.ejb.system.EjbSecurityQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoSsiQuery <SYSTEM extends JeeslIoSsiSystem<?,?>,
							CRED extends JeeslIoSsiCredential<SYSTEM>,
							CTX extends JeeslIoSsiContext<?,?>,
							STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
							ERROR extends JeeslIoSsiError<?,?,CTX,?>,
							ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
		extends AbstractEjbQuery
		implements JeeslIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoSsiQuery.class);
	private static final long serialVersionUID = 1L;
	
	public EjbIoSsiQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{
		super.reset();
		ioSsiContexts=null;
	}
	
	//Pagination
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> maxResults(Integer max) {super.setMaxResults(max); return this;}
	
//	public Boolean getTupleLoad() {return isTupleLoad();}
	
	//JEESL CQ
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(CqLong cq) {super.addCqLong(cq); return this;}
	
	//Fetches
	public <E extends Enum<E>> EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	// IO - SSI
	private List<SYSTEM> ioSsiSystems; @Override public List<SYSTEM> getIoSsiSystems() {return ioSsiSystems;} 
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(SYSTEM ejb) {if(Objects.isNull(ioSsiSystems)) {ioSsiSystems = new ArrayList<>();} ioSsiSystems.add(ejb); return this;}
	
	private List<CRED> ioSsiCredentials; @Override public List<CRED> getIoSsiCredentials() {return ioSsiCredentials;} 
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(CRED ejb) {if(Objects.isNull(ioSsiCredentials)) {ioSsiCredentials = new ArrayList<>();} ioSsiCredentials.add(ejb); return this;}

	
	private List<CTX> ioSsiContexts; @Override public List<CTX> getIoSsiContexts() {return ioSsiContexts;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(CTX ejb) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.add(ejb); return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> addContexts(List<CTX> list) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.addAll(list); return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> addContexts(Collection<CTX> collection) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.addAll(collection); return this;}
	
	private List<STATUS> ioSsiStatus; @Override public List<STATUS> getIoSsiStatus() {return ioSsiStatus;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(STATUS ejb) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.add(ejb); return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> addIoSsiStatus(List<STATUS> list) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.addAll(list); return this;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> addIoSsiStatus(Collection<STATUS> collection) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.addAll(collection); return this;}
	
	private List<ERROR> ioSsiError; @Override public List<ERROR> getIoSsiErrors() {return ioSsiError;}
	public EjbIoSsiQuery<SYSTEM,CRED,CTX,STATUS,ERROR,ENTITY> add(ERROR ejb) {if(Objects.isNull(ioSsiError)) {ioSsiError = new ArrayList<>();} ioSsiError.add(ejb); return this;}
}