package org.jeesl.interfaces.util.query.io;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiContext;
import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiLink;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoSsiQuery <CTX extends JeeslIoSsiContext<?,?>,
							STATUS extends JeeslIoSsiLink<?,?,STATUS,?>>
		extends AbstractEjbQuery
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
	
	//IDs
	public EjbIoSsiQuery<CTX,STATUS> id1(Long id1) {this.id1 = id1; return this;}
	public EjbIoSsiQuery<CTX,STATUS> id2(Long id2) {this.id2 = id2; return this;}
	public EjbIoSsiQuery<CTX,STATUS> id3(Long id3) {this.id3 = id3; return this;}
	
	//Fetches
	public <E extends Enum<E>> EjbIoSsiQuery<CTX,STATUS> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbIoSsiQuery<CTX,STATUS> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//Lists
	@Override public EjbIoSsiQuery<CTX,STATUS> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbIoSsiQuery<CTX,STATUS> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbIoSsiQuery<CTX,STATUS> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbIoSsiQuery<CTX,STATUS> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbIoSsiQuery<CTX,STATUS> ld1(LocalDate ld1) {this.ld1 = ld1; return this;}
	public EjbIoSsiQuery<CTX,STATUS> ld2(LocalDate ld2) {this.ld2 = ld2; return this;}
	public EjbIoSsiQuery<CTX,STATUS> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}

	private List<CTX> contexts; public List<CTX> getContexts() {return contexts;}
	public EjbIoSsiQuery<CTX,STATUS> add(CTX context) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.add(context); return this;}
	public EjbIoSsiQuery<CTX,STATUS> addContexts(List<CTX> list) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.addAll(list); return this;}
	public EjbIoSsiQuery<CTX,STATUS> addContexts(Collection<CTX> collection) {if(Objects.isNull(contexts)) {contexts = new ArrayList<>();} contexts.addAll(collection); return this;}
}