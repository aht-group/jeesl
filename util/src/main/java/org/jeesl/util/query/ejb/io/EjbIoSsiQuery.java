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
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.jeesl.interfaces.util.query.io.JeeslIoSsiQuery;
import org.jeesl.model.ejb.io.db.CqId;
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
		contexts=null;
	}
	
//	public Boolean getTupleLoad() {return isTupleLoad();}
	
	//IDs
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(CqId cq) {super.addId(cq); return this;}
	
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

	// IO SSI Objects
	private List<CTX> contexts; @Override public List<CTX> getIoSsiContexts() {return contexts;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(CTX context) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.add(context); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addContexts(List<CTX> list) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.addAll(list); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addContexts(Collection<CTX> collection) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.addAll(collection); return this;}
	
	private List<STATUS> status; @Override public List<STATUS> getIoSsiStatus() {return status;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(STATUS s) {if(Objects.isNull(status)) {status = new ArrayList<>();} status.add(s); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addStatus(List<STATUS> list) {if(Objects.isNull(status)) {status = new ArrayList<>();} status.addAll(list); return this;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> addStatus(Collection<STATUS> collection) {if(Objects.isNull(status)) {status = new ArrayList<>();} status.addAll(collection); return this;}
	
	private List<ERROR> errors; @Override public List<ERROR> getIoSsiErrors() {return errors;}
	public EjbIoSsiQuery<CTX,STATUS,ERROR> add(ERROR error) {if(Objects.isNull(errors)) {errors = new ArrayList<>();} errors.add(error); return this;}
}