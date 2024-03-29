package org.jeesl.util.query.ejb.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.AbstractEjbQuery;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.util.query.ejb.io.EjbIoDbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityQuery <CTX extends JeeslSecurityContext<?,?>>
					extends AbstractEjbQuery
					implements JeeslSecurityQuery<CTX>
{
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LoggerFactory.getLogger(EjbSecurityQuery.class);
	
	public EjbSecurityQuery()
	{       
		reset();
	}
	
	@Override public void reset()
	{

	}
	
	//Fetches
	public <E extends Enum<E>> EjbSecurityQuery<CTX> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbSecurityQuery<CTX> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//JEESL-CQ
	public EjbSecurityQuery<CTX> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbSecurityQuery<CTX> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbSecurityQuery<CTX> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbSecurityQuery<CTX> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbSecurityQuery<CTX> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbSecurityQuery<CTX> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbSecurityQuery<CTX> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbSecurityQuery<CTX> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CTX> securityContext; @Override public List<CTX> getSecurityContext() {return securityContext;}
}