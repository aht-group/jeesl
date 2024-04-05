package org.jeesl.util.query.ejb.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.io.JeeslIoSsiQuery;
import org.jeesl.util.query.cq.CqLong;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoSsiQuery <CTX extends JeeslIoSsiContext<?,?>,
							STATUS extends JeeslIoSsiStatus<?,?,STATUS,?>,
							ERROR extends JeeslIoSsiError<?,?,CTX,?>>
		extends AbstractEjbQuery
		implements JeeslIoSsiQuery<CTX,STATUS,ERROR>
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
	
//	public Boolean getTupleLoad() {return isTupleLoad();}
	
	//JEESL CQ
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(CqLong cq) {super.addCqLong(cq); return this;}
	
	//Fetches
	public <E extends Enum<E>> EjbIoSsiQuery<CTX,STATUS,ERROR> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbIoSsiQuery<CTX,STATUS,ERROR> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoSsiQuery<CTX,STATUS,ERROR> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoSsiQuery<CTX,STATUS,ERROR> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoSsiQuery<CTX,STATUS,ERROR> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoSsiQuery<CTX,STATUS,ERROR> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	// IO - SSI
	private List<CTX> ioSsiContexts; @Override public List<CTX> getIoSsiContexts() {return ioSsiContexts;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(CTX ejb) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.add(ejb); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addContexts(List<CTX> list) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.addAll(list); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addContexts(Collection<CTX> collection) {if(Objects.isNull(ioSsiContexts)) {ioSsiContexts = new ArrayList<>();} ioSsiContexts.addAll(collection); return this;}
	
	private List<STATUS> ioSsiStatus; @Override public List<STATUS> getIoSsiStatus() {return ioSsiStatus;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(STATUS ejb) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.add(ejb); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addStatus(List<STATUS> list) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.addAll(list); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addStatus(Collection<STATUS> collection) {if(Objects.isNull(ioSsiStatus)) {ioSsiStatus = new ArrayList<>();} ioSsiStatus.addAll(collection); return this;}
	
	private List<ERROR> ioSsiError; @Override public List<ERROR> getIoSsiErrors() {return ioSsiError;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(ERROR ejb) {if(Objects.isNull(ioSsiError)) {ioSsiError = new ArrayList<>();} ioSsiError.add(ejb); return this;}
}