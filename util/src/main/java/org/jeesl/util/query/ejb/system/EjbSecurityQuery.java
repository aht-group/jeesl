package org.jeesl.util.query.ejb.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.model.ejb.io.db.CqOrdering;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityQuery <CTX extends JeeslSecurityContext<?,?>,
								R extends JeeslSecurityRole<?,?,?,?,?,?>>
					extends AbstractEjbQuery
					implements JeeslSecurityQuery<CTX,R>
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
	public <E extends Enum<E>> EjbSecurityQuery<CTX,R> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbSecurityQuery<CTX,R> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//JEESL-CQ
	public EjbSecurityQuery<CTX,R> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbSecurityQuery<CTX,R> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbSecurityQuery<CTX,R> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbSecurityQuery<CTX,R> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbSecurityQuery<CTX,R> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbSecurityQuery<CTX,R> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbSecurityQuery<CTX,R> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbSecurityQuery<CTX,R> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<CTX> securityContext; @Override public List<CTX> getSecurityContext() {return securityContext;}
	public EjbSecurityQuery<CTX,R> add(CTX ejb) {if(Objects.isNull(securityContext)) {securityContext = new ArrayList<>();} securityContext.add(ejb); return this;}
	
	private List<R> securityRole; @Override public List<R> getSecurityRole() {return securityRole;}
	public EjbSecurityQuery<CTX,R> add(R ejb) {if(Objects.isNull(securityRole)) {securityRole = new ArrayList<>();} securityRole.add(ejb); return this;}
}