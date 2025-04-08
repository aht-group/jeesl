package org.jeesl.controller.facade.jx.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.controller.facade.jx.predicate.BooleanPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.LiteralPredicateBuilder;
import org.jeesl.controller.facade.jx.predicate.SortByPredicateBuilder;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
import org.jeesl.factory.ejb.system.security.EjbStaffFactory;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.access.JeeslStaff;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineHelp;
import org.jeesl.interfaces.model.system.security.doc.JeeslSecurityOnlineTutorial;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityArea;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory.Type;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.model.ejb.io.db.JeeslCqBoolean;
import org.jeesl.model.ejb.io.db.JeeslCqLiteral;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.util.query.cq.CqLiteral;
import org.jeesl.util.query.cq.CqOrdering;
import org.jeesl.util.query.ejb.system.EjbSecurityQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSecurityFacadeBean<C extends JeeslSecurityCategory<?,?>,
									R extends JeeslSecurityRole<?,?,C,V,U,A>,
									V extends JeeslSecurityView<?,?,C,R,U,A>,
									U extends JeeslSecurityUsecase<?,?,C,R,V,A>,
									A extends JeeslSecurityAction<?,?,R,V,U,?>,
									CTX extends JeeslSecurityContext<?,?>,
									M extends JeeslSecurityMenu<?,V,CTX,M>,
									AR extends JeeslSecurityArea<?,?,V>,
									OT extends JeeslSecurityOnlineTutorial<?,?,V>,
									OH extends JeeslSecurityOnlineHelp<V,?,?>,
									USER extends JeeslUser<R>>
							extends JeeslFacadeBean
							implements JeeslSecurityFacade<C,R,V,U,A,CTX,M,USER>
{	
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSecurityFacadeBean.class);
	
	private final SecurityFactoryBuilder<?,?,C,R,V,U,A,?,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity;
	
	public JeeslSecurityFacadeBean(EntityManager em, SecurityFactoryBuilder<?,?,C,R,V,U,A,?,CTX,M,AR,?,?,OT,OH,?,?,?,USER> fbSecurity)
	{
		super(em);
		this.fbSecurity=fbSecurity;
	}

	@Override public R load(R role)
	{
		role = em.find(fbSecurity.getClassRole(), role.getId());
		role.getCategory().getId();
		if(role.getActions()!=null){role.getActions().size();}
		if(role.getViews()!=null){role.getViews().size();}
		if(role.getUsecases()!=null){role.getUsecases().size();}
		return role;
	}
	
	@Override public V load(Class<V> cView, V view)
	{
		view = em.find(cView, view.getId());
		view.getCategory().getId();
		view.getActions().size();
		view.getRoles().size();
		view.getUsecases().size();
		return view;
	}
	
	@Override public U load(Class<U> cUsecase, U usecase)
	{
		usecase = em.find(cUsecase, usecase.getId());
		usecase.getCategory().getId();
		usecase.getActions().size();
		if(usecase.getViews()!=null){usecase.getViews().size();}
		if(usecase.getRoles()!=null){usecase.getRoles().size();}
		if(usecase.getActions()!=null){usecase.getActions().size();}
//		usecase.getTemplates().size();
		return usecase;
	}
	
	@Override public <E extends Enum<E>> C fSecurityCategory(Type type, E code)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaQuery<C> cQ = cB.createQuery(fbSecurity.getClassCategory());
		Root<C> reference = cQ.from(fbSecurity.getClassCategory());

		Expression<String> eType = reference.get(JeeslSecurityCategory.Attributes.type.toString());
		predicates.add(cB.equal(eType,type.toString()));
		
		Expression<String> eCode = reference.get(JeeslSecurityCategory.Attributes.code.toString());
		predicates.add(cB.equal(eCode,code.toString()));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(reference);
		
		TypedQuery<C> tQ = em.createQuery(cQ);
		return tQ.getSingleResult();
	}
	
	@Override public List<C> fSecurityCategories(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<C> cQ = cB.createQuery(fbSecurity.getClassCategory());
		Root<C> root = cQ.from(fbSecurity.getClassCategory());
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
		cQ.where(cB.and(pCategory(cB,query,root)));
		this.sortCategoryBy(cB,cQ,query,root);
		
		TypedQuery<C> tQ = em.createQuery(cQ);
		if(Objects.nonNull(query.getFirstResult())) {tQ.setFirstResult(query.getFirstResult());}
		if(Objects.nonNull(query.getMaxResults())) {tQ.setMaxResults(query.getMaxResults());}
		return tQ.getResultList();
	}
	
	@Override public List<V> allViewsForUser(USER user)
	{
		user = em.find(fbSecurity.getClassUserProject(), user.getId());
		Map<Long,V> views = new HashMap<Long,V>();
		for(R role : user.getRoles())
		{
			for(V rView : role.getViews())
			{
				views.put(rView.getId(), rView);
			}
			for(U u : role.getUsecases())
			{
				for(V uView : u.getViews())
				{
					views.put(uView.getId(), uView);
				}
			}
		}
		return new ArrayList<V>(views.values());
	}
	
	@Override public List<R> rolesForView(V view)
	{	
		return rolesForView(fbSecurity.getClassView(),null,view,null);
	}
	
	@Override public List<R> rolesForView(Class<V> cView, Class<USER> cUser, V view, USER user)
	{		
		view = em.find(cView, view.getId());
		Set<R> roles = new HashSet<R>();
		for(R r : view.getRoles())
		{
			if(!roles.contains(r)){roles.add(r);}
		}
		
		for(U u : view.getUsecases())
		{
			for(R r : u.getRoles())
			{
				if(!roles.contains(r)){roles.add(r);}
			}
		}
		return new ArrayList<R>(roles);
	}
	
//	@Override
	public List<R> rolesForView2(Class<V> cView, Class<USER> cUser, V view, USER user)
	{		
		view = em.find(cView, view.getId());
		Set<R> roles = new HashSet<R>();
		for(R r : view.getRoles())
		{
			if(!roles.contains(r)){roles.add(r);}
		}
		
		for(U u : view.getUsecases())
		{
			for(R r : u.getRoles())
			{
				if(!roles.contains(r)){roles.add(r);}
			}
		}
		return new ArrayList<R>(roles);
	}
	
	@Override public List<R> rolesForAction(Class<A> cAction, A action)
	{	
		return rolesForAction(cAction,null,action,null);
	}
	
	@Override public List<R> rolesForAction(Class<A> cAction, Class<USER> cUser, A action, USER user)
	{		
		action = em.find(cAction, action.getId());
		Map<Long,R> roles = new HashMap<Long,R>();
		for(R r : action.getRoles())
		{
			if(!roles.containsKey(r.getId())){roles.put(r.getId(), r);}
		}
		for(U u : action.getUsecases())
		{
			for(R r : u.getRoles())
			{
				if(!roles.containsKey(r.getId())){roles.put(r.getId(), r);}
			}
		}
		return new ArrayList<R>(roles.values());
	}
	
	@Override public List<A> allActionsForUser(USER user)
	{
		user = em.find(fbSecurity.getClassUserProject(), user.getId());
		Map<Long,A> actions = new HashMap<Long,A>();
		for(R r : user.getRoles())
		{
			for(A rAction : r.getActions())
			{
				actions.put(rAction.getId(), rAction);
			}
			for(U u : r.getUsecases())
			{
				for(A uAction : u.getActions())
				{
					actions.put(uAction.getId(), uAction);
				}
			}
		}
		return new ArrayList<A>(actions.values());
	}
	
	@Override public List<A> allActions(Class<R> cRole, List<R> roles)
	{
		List<A> result = new ArrayList<A>();
		logger.warn("NYI");
		return result;
	}
	
	@Override public boolean hasSecurityRole(USER user, R role)
	{
		if(ObjectUtils.anyNull(user,role)) {return false;}
		
		EjbSecurityQuery<C,R,V,U,A,CTX,USER> query = new EjbSecurityQuery<>();
		query.add(user);
		query.add(role);
		
		List<R> roles = this.fSecurityRoles(query);
		return !roles.isEmpty();
		
//		for(R r : allRolesForUser(user))
//		{
//			if(r.getId() == role.getId()) {return true;}
//		}
//		return false;
	}
	@Override public List<R> fSecurityRoles(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<R> cQ = cB.createQuery(fbSecurity.getClassRole());
		Root<R> root = cQ.from(fbSecurity.getClassRole());
		super.rootFetch(root, query);
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.addAll(Arrays.asList(pRole(cB,query,root)));
		
		if(ObjectUtils.isNotEmpty(query.getUsers()))
		{
			Root<USER> user = cQ.from(fbSecurity.getClassUserProject());
			ListJoin<USER,R> roles = user.joinList(JeeslUser.Attributes.roles.toString());
			predicates.add(cB.equal(root, roles));
			predicates.add(user.in(query.getUsers()));
			if(query.getUsers().size()>1) {cQ.distinct(true);}
		}
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		if(ObjectUtils.isNotEmpty(query.getCqOrderings()))
		{
			if(ObjectUtils.isNotEmpty(query.getUsers()) && query.getUsers().size()>1) {logger.warn("Ordering is deactivated for multiple users");}
			else {this.sortRoleBy(cB,cQ,query,root);}
		}
		
		TypedQuery<R> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<U> cQ = cB.createQuery(fbSecurity.getClassUsecase());
		Root<U> root = cQ.from(fbSecurity.getClassUsecase());
		super.rootFetch(root, query);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(ObjectUtils.isNotEmpty(query.getUsers()))
		{
			Root<USER> user = cQ.from(fbSecurity.getClassUserProject());
			ListJoin<USER,R> jRole = user.joinList(JeeslUser.Attributes.roles.toString());
			ListJoin<R,U> jUsecase = jRole.joinList(JeeslSecurityRole.Attributes.usecases.toString());
			
			predicates.add(cB.equal(jUsecase, root));
			predicates.add(user.in(query.getUsers()));
			cQ.distinct(true);
		}
		predicates.addAll(Arrays.asList(this.pUsecase(cB,query,root)));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		this.sortUsecase(cB,cQ,query,root);
		
		TypedQuery<U> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<A> fSecurityActions(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<A> cQ = cB.createQuery(fbSecurity.getClassAction());
		Root<A> root = cQ.from(fbSecurity.getClassAction());
		super.rootFetch(root, query);
		
		Join<A,V> jActionView = null;
		ListJoin<V,R> jViewRole = null;
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(ObjectUtils.isNotEmpty(query.getUsers()))
		{
			Root<USER> user = cQ.from(fbSecurity.getClassUserProject());
			ListJoin<USER,R> roles = user.joinList(JeeslUser.Attributes.roles.toString());
			
			jActionView = root.join(JeeslSecurityAction.Attributes.view.toString());
			jViewRole = jActionView.joinList(JeeslSecurityView.Attributes.roles.toString());
			
			predicates.add(cB.equal(jViewRole, roles));
			predicates.add(user.in(query.getUsers()));
			cQ.distinct(true);
		}
		
		predicates.addAll(Arrays.asList(pAction(cB,query,root,jActionView)));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		if(ObjectUtils.isEmpty(query.getUsers())) {this.sortAction(cB,cQ,query,root);}
		else  {logger.warn("Ordering is deactivated when a user is active");}

		
		TypedQuery<A> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	private Predicate[] pAction(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<A> root, Join<A,V> jActionView)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getSecurityView()))
		{
			if(Objects.isNull(jActionView)) {jActionView = root.join(JeeslSecurityAction.Attributes.view.toString());}
			predicates.add(jActionView.in(query.getSecurityView()));
		}
		if(ObjectUtils.isNotEmpty(query.getSecurityCategory()))
		{
			if(Objects.isNull(jActionView)) {jActionView = root.join(JeeslSecurityAction.Attributes.view.toString());}
			Path<C> pCategory = jActionView.get(JeeslSecurityView.Attributes.category.toString());
			predicates.add(pCategory.in(query.getSecurityCategory()));
		}
		
		for(JeeslCqBoolean c : ListUtils.emptyIfNull(query.getCqBooleans()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslSecurityRole.Attributes.visible))) //dummy
			{
				BooleanPredicateBuilder.add(cB,predicates,c,root.<Boolean>get(JeeslSecurityRole.Attributes.visible.toString()));
			}
			else {logger.warn("NYI "+JeeslCqBoolean.class.getSimpleName()+" path: "+c.toString());}
		}
		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslSecurityAction.Attributes.code))) //dummy
			{
				LiteralPredicateBuilder.add(cB,predicates,cq,root.<String>get(JeeslSecurityAction.Attributes.code.toString()));
			}
			else {logger.warn(cq.nyi(JeeslSecurityAction.class));}
		}
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortAction(CriteriaBuilder cB, CriteriaQuery<A> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<A> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
//			if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.position)))
//			{
//				Expression<Integer> ePosition = root.get(JeeslSecurityRole.Attributes.position.toString());
//				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
//			}
//			else if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.category,JeeslSecurityCategory.Attributes.position)))
//			{
//				Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
//				Expression<Integer> ePosition = pCategory.get(JeeslSecurityCategory.Attributes.position.toString());
//				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
//			}
//			
//			else {logger.warn("No SortBy Handling for "+c.toString());}	
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	@Override public List<M> fSecurityMenus(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<M> cQ = cB.createQuery(fbSecurity.getClassMenu());
		Root<M> root = cQ.from(fbSecurity.getClassMenu());
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
		cQ.where(cB.and(this.pMenu(cB,query,root)));
		this.sortMenuBy(cB,cQ,query,root);
		
		TypedQuery<M> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public List<USER> fUsers(R role)
	{		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<USER> cQ = cB.createQuery(fbSecurity.getClassUserProject());
		Root<USER> root = cQ.from(fbSecurity.getClassUserProject());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		ListJoin<USER,R> jRole = root.joinList(JeeslUser.Attributes.roles.toString());
		predicates.add(cB.equal(jRole,role));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<USER> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<USER> fSecurityUsers(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<USER> cQ = cB.createQuery(fbSecurity.getClassUserProject());
		Root<USER> root = cQ.from(fbSecurity.getClassUserProject());
		super.rootFetch(root, query);
		
		cQ.select(root);
		cQ.where(cB.and(this.pUser(cB,query,root)));
		this.sortByUser(cB,cQ,query,root);
		
		TypedQuery<USER> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Class<S> cStaff)
	{
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<S> cQ = cB.createQuery(cStaff);
//		Root<S> root = cQ.from(cStaff);
//		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
//		
//		cQ.select(root);
//		cQ.where(cB.and(this.pStaff(cB,query,root,cStaff)));
//		
//		TypedQuery<S> tQ = em.createQuery(cQ);
//		super.pagination(tQ, query);
//		return tQ.getResultList();
		return null;
	}
	
	@Override public List<R> allRolesForUser(USER user)
	{
		user = em.find(fbSecurity.getClassUserProject(), user.getId());
		user.getRoles().size();
		return user.getRoles();
	}
	
	@Override public <WC extends JeeslSecurityWithCategory<C>> List<WC> allForCategory(Class<WC> clWc, Class<C> clC, String code) throws JeeslNotFoundException
	{
		if(logger.isTraceEnabled())
		{
			logger.info(clWc.getName());
			logger.info(JeeslSecurityRole.class.getSimpleName()+" ");
			logger.info(JeeslSecurityRole.class.getSimpleName()+" "+clWc.isAssignableFrom(JeeslSecurityRole.class));
			logger.info(JeeslSecurityView.class.getSimpleName()+" "+clWc.isAssignableFrom(JeeslSecurityView.class));
			logger.info(JeeslSecurityUsecase.class.getSimpleName()+" "+clWc.isAssignableFrom(JeeslSecurityUsecase.class));
		}
	
		String type = EjbSecurityCategoryFactory.toType(clWc);
		
		C category = this.fByTypeCode(clC, type, code);
		
		return this.allOrderedPositionParent(clWc,category);
	}	

	
	// STAFF
	@Override
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId>
		List<S> fStaffU(Class<S> clStaff, USER user)
	{return allForParent(clStaff,JeeslStaff.Attributes.user, user);}
	
//	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffR(Class<S> clStaff, R role) {return allForParent(clStaff,JeeslStaff.Attributes.role, role);}
	
	@Override
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId>
		List<S> fStaffD(Class<S> clStaff, D1 domain)
	{return allForParent(clStaff, JeeslStaff.Attributes.domain, domain);}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffD(Class<S> cStaff, List<D1> domains)
	{
		if(domains==null || domains.isEmpty()) {return new ArrayList<S>();}
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<S> cQ = cB.createQuery(cStaff);
		Root<S> staff = cQ.from(cStaff);

		Path<D1> pDomain = staff.get(JeeslStaff.Attributes.domain.toString());
		predicates.add(cB.isTrue(pDomain.in(domains)));

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(staff);
		return em.createQuery(cQ).getResultList();
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId>
		List<S> fStaffUR(Class<S> clStaff, USER user, R role)
	{return allForParent(clStaff, JeeslStaff.Attributes.user,user, JeeslStaff.Attributes.role,role);}
	
	@Override
	public <S extends JeeslStaff<R, USER, D1, D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUR(Class<S> cStaff, USER user, List<R> roles)
	{
		if(roles==null || roles.isEmpty()){return new ArrayList<S>();}
		List<USER> users = new ArrayList<USER>(Arrays.asList(user)); 
		List<D1> domains = null;
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId>
		List<S> fStaffUD(Class<S> cStaff, USER user, D1 domain)
	{
		List<USER> users = new ArrayList<USER>(Arrays.asList(user));
		List<R> roles = null;
		List<D1> domains = new ArrayList<D1>(Arrays.asList(domain));
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, D1 domain)
	{
		List<USER> users = null;
		List<R> roles = new ArrayList<R>(Arrays.asList(role));
		List<D1> domains = new ArrayList<D1>(Arrays.asList(domain));
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffURD(Class<S> cStaff, USER user, R role, List<D1> domains)
	{
		if(domains==null || domains.isEmpty()){return new ArrayList<S>();}
		List<USER> users = new ArrayList<USER>(Arrays.asList(user));
		List<R> roles = new ArrayList<R>(Arrays.asList(role));
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, List<D1> domains)
	{
		if(domains==null || domains.isEmpty()){return new ArrayList<S>();}
		List<USER> users = null;
		List<R> roles = new ArrayList<R>(Arrays.asList(role));	
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, List<R> roles, List<D1> domains)
	{
		if(domains==null || domains.isEmpty() || roles==null || roles.isEmpty()){return new ArrayList<S>();}
		List<USER> users = null;
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUD(Class<S> cStaff, USER user, List<D1> domains)
	{
		if(domains==null || domains.isEmpty()){return new ArrayList<S>();}
		List<USER> users = new ArrayList<USER>(Arrays.asList(user));
		List<R> roles = null;
		return fStaffURD(cStaff,users,roles,domains);
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaff(Class<S> cStaff, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<S> cQ = cB.createQuery(cStaff);
		Root<S> root = cQ.from(cStaff);
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
		cQ.where(cB.and(this.pStaff(cB,query,root,cStaff)));
		
		TypedQuery<S> tQ = em.createQuery(cQ);
		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffURD(Class<S> cStaff, List<USER> users, List<R> roles, List<D1> domains)
	{
		if(users==null && roles==null && domains==null) {return new ArrayList<S>();}
		if(users!=null && users.isEmpty()){return new ArrayList<S>();}
		if(roles!=null && roles.isEmpty()){return new ArrayList<S>();}
		if(domains!=null && domains.isEmpty()){return new ArrayList<S>();}
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<S> cQ = cB.createQuery(cStaff);
		Root<S> staff = cQ.from(cStaff);

		if(users!=null)
		{
			Path<USER> pUser = staff.get(JeeslStaff.Attributes.user.toString());
			predicates.add(pUser.in(users));
		}
		
		if(roles!=null)
		{
			Path<R> pRole = staff.get(JeeslStaff.Attributes.role.toString());
			predicates.add(pRole.in(roles));
		}
		
		if(domains!=null)
		{
			Path<D1> pDomain = staff.get(JeeslStaff.Attributes.domain.toString());
			predicates.add(pDomain.in(domains));
		}

		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		cQ.select(staff);
		return em.createQuery(cQ).getResultList();
	}
	
	@Override
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> S fStaff(Class<S> clStaff, USER user, R role, D1 domain) throws JeeslNotFoundException
	{
		return oneForParents(clStaff,JeeslStaff.Attributes.user,user,JeeslStaff.Attributes.role,role,JeeslStaff.Attributes.domain,domain);
	}
	
	@Override public void grantRole(Class<USER> clUser, Class<R> clRole, USER user, R role, boolean grant)
	{
		logger.info("grantRole u:"+user.toString()+" r:"+role.toString()+" grant:"+grant);
		user = em.find(clUser,user.getId());
		role = em.find(clRole,role.getId());
		if(grant){addRole(user, role);}
		else{rmRole(user, role);}
		em.merge(user);
	}
	
	private void addRole(USER user, R role)
	{
		logger.info("addRole u:"+user.toString()+" r:"+role.toString());
		if(!user.getRoles().contains(role))
		{
			logger.info("Role does not exist for user, adding.");
//			role.getUsers().add(user);
			user.getRoles().add(role);
		}
		em.merge(user);
	}
	
	private void rmRole(USER user, R role)
	{
		if(user.getRoles().contains(role)){user.getRoles().remove(role);}
//		if(role.getUsers().contains(user)){role.getUsers().remove(user);}
		user = em.merge(user);
//		role = em.merge(role);
	}

	@Override
	public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<D1> fDomains(Class<V> cView, Class<S> cStaff, USER user, V view)
	{
		List<R> roles = new ArrayList<R>();
		List<D1> result = new ArrayList<D1>();
		
		view = this.find(cView, view);
		roles.addAll(view.getRoles());
		
//		logger.info(StringUtil.stars());

		for(U u : view.getUsecases())
		{
			for(R role : u.getRoles())
			{
//				logger.info("\t\tR"+role.toString());
				if(!roles.contains(role))
				{
					roles.add(role);
				}
			}
		}
		
//		logger.info("Roles to check: "+roles.size());
		
		for(R role : roles)
		{
//			logger.info("\tR"+role.toString());
			for(S staff : this.fStaffUR(cStaff, user, role))
			{
				if(!result.contains(staff.getDomain()))
				{
					result.add(staff.getDomain());
				}
			}
		}
		return result;
	}
	
	private Predicate[] pCategory(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<C> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		for(JeeslCqLiteral c : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslSecurityCategory.Attributes.type)))
			{
				Expression<String> e = root.get(JeeslSecurityCategory.Attributes.type.toString());
				LiteralPredicateBuilder.add(cB,predicates,c,e);
			}
			else if(c.getPath().equals(CqLiteral.path(JeeslSecurityCategory.Attributes.code)))
			{
				Expression<String> e = root.get(JeeslSecurityCategory.Attributes.code.toString());
				LiteralPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI "+JeeslCqLiteral.class.getSimpleName()+" path: "+c.toString());}
		}
		for(JeeslCqBoolean c : ListUtils.emptyIfNull(query.getCqBooleans()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslSecurityCategory.Attributes.visible)))
			{
				Expression<Boolean> e = root.get(JeeslSecurityCategory.Attributes.visible.toString());
				BooleanPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI "+JeeslCqBoolean.class.getSimpleName()+" path: "+c.toString());}
		}
		
		if(ObjectUtils.isNotEmpty(query.getSecurityContext()))
		{
			Path<CTX> pCtx = root.get(JeeslSecurityMenu.Attributes.context.toString());
			predicates.add(pCtx.in(query.getSecurityContext()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortCategoryBy(CriteriaBuilder cB, CriteriaQuery<C> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<C> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(c.getPath().equals(CqOrdering.path(JeeslSecurityCategory.Attributes.position)))
			{
				Expression<Integer> ePosition = root.get(JeeslSecurityCategory.Attributes.position.toString());
				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
			}
			else {logger.warn("No SortBy Handling for "+c.toString());}	
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	private Predicate[] pRole(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<R> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		if(ObjectUtils.isNotEmpty(query.getSecurityRole())) {predicates.add(root.in(query.getSecurityRole()));}
		
		if(ObjectUtils.isNotEmpty(query.getSecurityCategory()))
		{
			Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
			predicates.add(pCategory.in(query.getSecurityCategory()));
		}
		
		for(JeeslCqBoolean c : ListUtils.emptyIfNull(query.getCqBooleans()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslSecurityRole.Attributes.visible)))
			{
				Expression<Boolean> e = root.get(JeeslSecurityRole.Attributes.visible.toString());
				BooleanPredicateBuilder.add(cB,predicates,c,e);
			}
			else if(c.getPath().equals(CqLiteral.path(JeeslSecurityRole.Attributes.category,JeeslSecurityCategory.Attributes.visible)))
			{
				Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
				Expression<Boolean> e = pCategory.get(JeeslSecurityRole.Attributes.visible.toString());
				BooleanPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI "+JeeslCqBoolean.class.getSimpleName()+" path: "+c.toString());}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortRoleBy(CriteriaBuilder cB, CriteriaQuery<R> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<R> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.position)))
			{
				Expression<Integer> ePosition = root.get(JeeslSecurityRole.Attributes.position.toString());
				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
			}
			else if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.category,JeeslSecurityCategory.Attributes.position)))
			{
				Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
				Expression<Integer> ePosition = pCategory.get(JeeslSecurityCategory.Attributes.position.toString());
				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
			}
			
			else {logger.warn("No SortBy Handling for "+c.toString());}	
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	private Predicate[] pUsecase(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<U> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getSecurityUsecase())) {predicates.add(root.in(query.getSecurityUsecase()));}
		if(ObjectUtils.isNotEmpty(query.getSecurityCategory()))
		{
			Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
			predicates.add(pCategory.in(query.getSecurityCategory()));
		}
		
		for(JeeslCqBoolean c : ListUtils.emptyIfNull(query.getCqBooleans()))
		{
			if(c.getPath().equals(CqLiteral.path(JeeslSecurityRole.Attributes.visible)))
			{
				Expression<Boolean> e = root.get(JeeslSecurityRole.Attributes.visible.toString());
				BooleanPredicateBuilder.add(cB,predicates,c,e);
			}
			else if(c.getPath().equals(CqLiteral.path(JeeslSecurityRole.Attributes.category,JeeslSecurityCategory.Attributes.visible)))
			{
				Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
				Expression<Boolean> e = pCategory.get(JeeslSecurityRole.Attributes.visible.toString());
				BooleanPredicateBuilder.add(cB,predicates,c,e);
			}
			else {logger.warn("NYI "+JeeslCqBoolean.class.getSimpleName()+" path: "+c.toString());}
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortUsecase(CriteriaBuilder cB, CriteriaQuery<U> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<U> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
//			if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.position)))
//			{
//				Expression<Integer> ePosition = root.get(JeeslSecurityRole.Attributes.position.toString());
//				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
//			}
//			else if(c.getPath().equals(CqOrdering.path(JeeslSecurityRole.Attributes.category,JeeslSecurityCategory.Attributes.position)))
//			{
//				Path<C> pCategory = root.get(JeeslSecurityRole.Attributes.category.toString());
//				Expression<Integer> ePosition = pCategory.get(JeeslSecurityCategory.Attributes.position.toString());
//				SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
//			}
//			
//			else {logger.warn("No SortBy Handling for "+c.toString());}	
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	
	
	private Predicate[] pMenu(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<M> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getSecurityContext()))
		{
			Path<CTX> pCtx = root.get(JeeslSecurityMenu.Attributes.context.toString());
			predicates.add(pCtx.in(query.getSecurityContext()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortMenuBy(CriteriaBuilder cB, CriteriaQuery<M> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<M> root)
	{
		if(ObjectUtils.isNotEmpty(query.getCqOrderings()))
		{
			List<Order> orders = new ArrayList<>();
			for(JeeslCqOrdering c : query.getCqOrderings())
			{
				if(c.getPath().equals(CqOrdering.path(JeeslSecurityMenu.Attributes.position)))
				{
					Expression<Integer> ePosition = root.get(JeeslSecurityMenu.Attributes.position.toString());
					SortByPredicateBuilder.addByInteger(cB,orders,c,ePosition);
				}
				else if(c.getPath().equals(CqOrdering.path(JeeslSecurityMenu.Attributes.parent,JeeslSecurityMenu.Attributes.id)))
				{
					Path<M> pParent = root.get(JeeslSecurityMenu.Attributes.parent.toString());
					Expression<Long> eId = pParent.get(JeeslSecurityMenu.Attributes.id.toString());
					SortByPredicateBuilder.addByLong(cB,orders,c,eId);
				}
				else {logger.warn("No SortBy Handling for "+c.toString());}
			}
			if(!orders.isEmpty()) {cQ.orderBy(orders);}
		}
	}
	
	
	private Predicate[] pUser(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<USER> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		ListJoin<USER,R> jRole = null;
		
		if(ObjectUtils.isNotEmpty(query.getSecurityRole()))
		{
			if(Objects.isNull(jRole)) {jRole = root.joinList(JeeslUser.Attributes.roles.toString());}
			predicates.add(jRole.in(query.getSecurityRole()));
		}
		
		for(JeeslCqLiteral cq : ListUtils.emptyIfNull(query.getCqLiterals()))
		{
			if(cq.getPath().equals(CqLiteral.path(JeeslUser.Attributes.email)))
			{
				LiteralPredicateBuilder.add(cB,predicates,cq,root.<String>get(JeeslUser.Attributes.email.toString()));
			}
			else {logger.warn(cq.nyi(JeeslSecurityAction.class));}
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortByUser(CriteriaBuilder cB, CriteriaQuery<USER> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<USER> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
		{
			if(c.getPath().equals(CqOrdering.path(JeeslUser.Attributes.id)))
			{
				SortByPredicateBuilder.addByLong(cB,orders,c,root.<Long>get(JeeslUser.Attributes.id.toString()));
			}
			else {logger.warn(c.nyi(JeeslUser.class));}
		}
		if(!orders.isEmpty()) {cQ.orderBy(orders);}
	}
	
	
	private <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> Predicate[] pStaff(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<S> root, Class<S> cStaff)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		Join<S,R> jRole = null;//root.get(JeeslStaff.Attributes.role.toString());
		
		if(ObjectUtils.isNotEmpty(query.getSecurityRole()))
		{
			if(Objects.isNull(jRole)) {jRole = root.join(JeeslStaff.Attributes.role.toString());}
			predicates.add(jRole.in(query.getSecurityRole()));
		}
		if(ObjectUtils.isNotEmpty(query.getSecurityUsecase()))
		{
			if(Objects.isNull(jRole)) {jRole = root.join(JeeslStaff.Attributes.role.toString());}
			ListJoin<R,U> jUsecase = jRole.joinList(JeeslSecurityRole.Attributes.usecases.toString());
			predicates.add(jUsecase.in(query.getSecurityUsecase()));
		}
		if(ObjectUtils.isNotEmpty(query.getUsers()))
		{
			Path<USER> pUser = root.get(JeeslStaff.Attributes.user.toString());
			predicates.add(pUser.in(query.getUsers()));
		}
		if(ObjectUtils.isNotEmpty(query.getStaffDomains()))
		{
			List<EjbWithId> domains = EjbStaffFactory.toDomains(query.getStaffDomains(),cStaff);
			
			Path<?> pDomain = root.get(JeeslStaff.Attributes.domain.toString());
			predicates.add(pDomain.in(query.getStaffDomains()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}