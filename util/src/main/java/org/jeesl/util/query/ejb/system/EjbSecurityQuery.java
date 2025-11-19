package org.jeesl.util.query.ejb.system;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.AbstractEjbQuery;
import org.jeesl.util.query.ejb.module.EjbAomQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbSecurityQuery <C extends JeeslSecurityCategory<?,?>,
								R extends JeeslSecurityRole<?,?,C,V,U,A>,
								V extends JeeslSecurityView<?,?,C,R,U,A>,
								U extends JeeslSecurityUsecase<?,?,C,R,V,A>,
								A extends JeeslSecurityAction<?,?,R,V,U,?>,
								CTX extends JeeslSecurityContext<?,?>,
								USER extends JeeslUser<R>>
					extends AbstractEjbQuery
					implements JeeslSecurityQuery<C,R,V,U,A,CTX,USER>
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
	
	//Pagination
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> maxResults(Integer max) {super.setMaxResults(max); return this;}
	
	//Fetches
	public <E extends Enum<E>> EjbSecurityQuery<C,R,V,U,A,CTX,USER> addRootFetch(E e){if(rootFetches==null) {rootFetches = new ArrayList<>();} rootFetches.add(e.toString()); return this;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> distinct(boolean distinct) {super.setDistinct(distinct); return this;}
	
	//JEESL-CQ
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(JeeslCqBoolean bool) {super.addCqBoolean(bool); return this;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(JeeslCqLiteral bool) {super.addCqLiteral(bool); return this;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> orderBy(CqOrdering ordering) {super.addOrdering(ordering); return this;}
	
	//Lists
	@Override public EjbSecurityQuery<C,R,V,U,A,CTX,USER> id(EjbWithId id) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.add(id.getId()); return this;}
	@Override public <T extends EjbWithId> EjbSecurityQuery<C,R,V,U,A,CTX,USER> ids(List<T> ids) {logger.error("NYI"); return this;}
	@Override public EjbSecurityQuery<C,R,V,U,A,CTX,USER> idList(List<Long> list) {if(Objects.isNull(idList)) {idList = new ArrayList<>();} idList.addAll(list); return this;}
	@Override public EjbSecurityQuery<C,R,V,U,A,CTX,USER> codeList(List<String> list) {if(Objects.isNull(codeList)) {codeList = new ArrayList<>();} codeList.addAll(list); return this;}

	//LocalDate
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> ld1(LocalDate ld1) {this.localDate1 = ld1; return this;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> ld2(LocalDate ld2) {this.localDate2 = ld2; return this;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> ld3(LocalDate ld3) {this.ld3 = ld3; return this;}
	
	// System - Security
	private List<C> securityCategory;
	@Override public List<C> getSecurityCategory() {return securityCategory;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(C ejb) {if(Objects.isNull(securityCategory)) {securityCategory = new ArrayList<>();} securityCategory.add(ejb); return this;}
	
	private List<R> securityRole;
	@Override public List<R> getSecurityRole() {return securityRole;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(R ejb) {if(Objects.isNull(securityRole)) {securityRole = new ArrayList<>();} securityRole.add(ejb); return this;}
	
	private List<U> securityUsecase;
	@Override public List<U> getSecurityUsecase() {return securityUsecase;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(U ejb) {if(Objects.isNull(securityUsecase)) {securityUsecase = new ArrayList<>();} securityUsecase.add(ejb); return this;}

	
	private List<V> securityView;
	@Override public List<V> getSecurityView() {return securityView;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(V ejb) {if(Objects.isNull(securityView)) {securityView = new ArrayList<>();} securityView.add(ejb); return this;}

	private List<A> securityAction;
	@Override public List<A> getSecurityAction() {return securityAction;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(A ejb) {if(Objects.isNull(securityAction)) {securityAction = new ArrayList<>();} securityAction.add(ejb); return this;}

	private List<CTX> securityContext;
	@Override public List<CTX> getSecurityContext() {return securityContext;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(CTX ejb) {if(Objects.isNull(securityContext)) {securityContext = new ArrayList<>();} securityContext.add(ejb); return this;}
	
	private List<USER> securityUser;
	@Override public List<USER> getUsers() {return securityUser;}
	public EjbSecurityQuery<C,R,V,U,A,CTX,USER> add(USER ejb) {if(Objects.isNull(securityUser)) {securityUser = new ArrayList<>();} securityUser.add(ejb); return this;}
	
	
    public <E extends Enum<E>> A querySingle(JeeslSecurityFacade<C,R,V,U,A,CTX,?,USER> fSecurity, V view, E actionCode) throws JeeslNotFoundException
    {
    	EjbSecurityQuery<C,R,V,U,A,CTX,USER> query = new EjbSecurityQuery<>();
    	query.add(view);
    	query.add(CqLiteral.exact("mfaDaily",CqLiteral.path(actionCode)));
		
    	List<A> list = fSecurity.fSecurityActions(query);
    	if(list.isEmpty()) {throw new JeeslNotFoundException("NotFound A");}
    	if(list.size()>1) {throw new JeeslNotFoundException("Multiple Results");}
    	
    	return list.get(0);
    }
}