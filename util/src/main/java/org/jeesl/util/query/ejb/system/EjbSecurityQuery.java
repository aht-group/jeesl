package org.jeesl.util.query.ejb.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityQuery <C extends JeeslSecurityCategory<?,?>,
								R extends JeeslSecurityRole<?,?,C,?,?,?>,
								CTX extends JeeslSecurityContext<?,?>>
					extends AbstractEjbQuery
					implements JeeslSecurityQuery<C,R,CTX>
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
	public <E extends Enum<E>> EjbSecurityQuery<C,R,CTX> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbSecurityQuery<C,R,CTX> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//JEESL-CQ
	public EjbSecurityQuery<C,R,CTX> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbSecurityQuery<C,R,CTX> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbSecurityQuery<C,R,CTX> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbSecurityQuery<C,R,CTX> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbSecurityQuery<C,R,CTX> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbSecurityQuery<C,R,CTX> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbSecurityQuery<C,R,CTX> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbSecurityQuery<C,R,CTX> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	private List<C> securityCategory;
	@Override public List<C> getSecurityCategory() {return securityCategory;}
	public EjbSecurityQuery<C,R,CTX> add(C ejb) {if(Objects.isNull(securityCategory)) {securityCategory = new ArrayList<>();} securityCategory.add(ejb); return this;}
	
	private List<R> securityRole;
	@Override public List<R> getSecurityRole() {return securityRole;}
	public EjbSecurityQuery<C,R,CTX> add(R ejb) {if(Objects.isNull(securityRole)) {securityRole = new ArrayList<>();} securityRole.add(ejb); return this;}

	private List<CTX> securityContext;
	@Override public List<CTX> getSecurityContext() {return securityContext;}
	public EjbSecurityQuery<C,R,CTX> add(CTX ejb) {if(Objects.isNull(securityContext)) {securityContext = new ArrayList<>();} securityContext.add(ejb); return this;}
}