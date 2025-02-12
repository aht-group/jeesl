package org.jeesl.controller.facade.jk.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.controller.facade.jk.JeeslFacadeBean;
import org.jeesl.controller.facade.jk.predicate.SortByPredicateBuilder;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.ejb.system.security.EjbSecurityCategoryFactory;
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
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory.Type;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;
import org.jeesl.util.query.cq.CqOrdering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
//	@Override public R load(R role, boolean withUsers)
//	{
//		role = em.find(fbSecurity.getClassRole(), role.getId());
//		role.getCategory().getId();
//		if(withUsers && role.getUsers()!=null){role.getUsers().size();}
//		if(role.getActions()!=null){role.getActions().size();}
//		if(role.getViews()!=null){role.getViews().size();}
//		if(role.getUsecases()!=null){role.getUsecases().size();}
//		return role;
//	}
	
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
//		this.sortMenuBy(cB,cQ,query,root);
		
		TypedQuery<C> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<R> fSecurityRoles(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<R> cQ = cB.createQuery(fbSecurity.getClassRole());
		Root<R> root = cQ.from(fbSecurity.getClassRole());
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
		cQ.where(cB.and(pRole(cB,query,root)));
//		this.sortMenuBy(cB,cQ,query,root);
		
		TypedQuery<R> tQ = em.createQuery(cQ);
		if(Objects.nonNull(query.getFirstResult())) {tQ.setFirstResult(query.getFirstResult());}
		if(Objects.nonNull(query.getMaxResults())) {tQ.setMaxResults(query.getMaxResults());}
		return tQ.getResultList();
	}
	
	@Override public List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public List<A> fSecurityActions(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public List<M> fSecurityMenus(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<M> cQ = cB.createQuery(fbSecurity.getClassMenu());
		Root<M> root = cQ.from(fbSecurity.getClassMenu());
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<M> tQ = em.createQuery(cQ);
		return tQ.getResultList();
	}
	
	@Override public List<USER> fSecurityUsers(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query)
	{
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<USER> cQ = cB.createQuery(fbSecurity.getClassUserProject());
		Root<USER> root = cQ.from(fbSecurity.getClassUserProject());
//		super.rootFetch(root, query);
		
		cQ.select(root);
		cQ.where(cB.and(this.pUser(cB,query,root)));
		this.sortByUser(cB,cQ,query,root);
		
		TypedQuery<USER> tQ = em.createQuery(cQ);
//		super.pagination(tQ, query);
		return tQ.getResultList();
	}
	
	@Override public <S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Class<S> cStaff)
	{
		// TODO Auto-generated method stub
		return null;
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
	
	@Override public List<USER> fUsers(R role)
	{
//		CriteriaBuilder cB = em.getCriteriaBuilder();
//		CriteriaQuery<G> cQ = cB.createQuery(cG);
//		Root<W> root = cQ.from(c);
//		
//		Path<G> pGraphic = root.get("graphic");
//		Path<Long> pId = root.get("id");
//		
//		cQ.select(pGraphic);
//		cQ.where(cB.equal(pId,id));
		
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<USER> cQ = cB.createQuery(fbSecurity.getClassUserProject());
		Root<USER> root = cQ.from(fbSecurity.getClassUserProject());
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		ListJoin<USER,R> jRole = root.joinList(JeeslUser.Attributes.roles.toString());
		predicates.add(cB.equal(jRole,role));
		
		cQ.select(root);
		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		return em.createQuery(cQ).getResultList();
		
//		TypedQuery<VIEW> tQ = em.createQuery(cQ);
//		try	{return tQ.getSingleResult();}
//		catch (NoResultException ex){throw new JeeslNotFoundException(ex.getMessage());}
//		
//		
//		role = em.find(fbSecurity.getClassRole(), role.getId());
//		return new ArrayList<>(role.getUsers());
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
	
	@Override public List<R> allRolesForUser(USER user)
	{
		user = em.find(fbSecurity.getClassUserProject(), user.getId());
		user.getRoles().size();
		return user.getRoles();
	}
	
	@Override public <WC extends JeeslSecurityWithCategory<C>> List<WC> allForCategory(Class<WC> cWithCategory, Class<C> cCategory, String code) throws JeeslNotFoundException
	{
		if(logger.isTraceEnabled())
		{
			logger.info(cWithCategory.getName());
			logger.info(JeeslSecurityRole.class.getSimpleName()+" ");
			logger.info(JeeslSecurityRole.class.getSimpleName()+" "+cWithCategory.isAssignableFrom(JeeslSecurityRole.class));
			logger.info(JeeslSecurityView.class.getSimpleName()+" "+cWithCategory.isAssignableFrom(JeeslSecurityView.class));
			logger.info(JeeslSecurityUsecase.class.getSimpleName()+" "+cWithCategory.isAssignableFrom(JeeslSecurityUsecase.class));
		}
	
		String type = EjbSecurityCategoryFactory.toType(cWithCategory);
		logger.info("Type: "+type);
		
		C category = this.fByTypeCode(cCategory, type, code);
		
		return this.allOrderedPositionParent(cWithCategory,category);
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
		if(domains==null || domains.isEmpty()){return new ArrayList<S>();}
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
		List<Predicate> predicates = new ArrayList<Predicate>();
		CriteriaBuilder cB = em.getCriteriaBuilder();
		CriteriaQuery<S> cQ = cB.createQuery(cStaff);
		Root<S> root = cQ.from(cStaff);
		if(query.getRootFetches()!=null) {for(String fetch : query.getRootFetches()) {root.fetch(fetch, JoinType.LEFT);}}
		
		cQ.select(root);
//		cQ.where(cB.and(predicates.toArray(new Predicate[predicates.size()])));
		
		TypedQuery<S> tQ = em.createQuery(cQ);
		if(query.getFirstResult()!=null){tQ.setFirstResult(query.getFirstResult());}
		if(query.getMaxResults()!=null){tQ.setMaxResults(query.getMaxResults());}
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

	@Override public boolean hasSecurityRole(USER user, R role)
	{
		if(user==null || role==null){return false;}
		for(R r: allRolesForUser(user))
		{
			if(r.getId() == role.getId()){return true;}
		}
		return false;
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
	
	private Predicate[] pRole(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<R> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private Predicate[] pCategory(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<C> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(ObjectUtils.isNotEmpty(query.getSecurityContext()))
		{
			Path<CTX> pCtx = root.get(JeeslSecurityMenu.Attributes.context.toString());
			predicates.add(pCtx.in(query.getSecurityContext()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private Predicate[] pUser(CriteriaBuilder cB, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<USER> root)
	{
		List<Predicate> predicates = new ArrayList<Predicate>();
			
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	private void sortByUser(CriteriaBuilder cB, CriteriaQuery<USER> cQ, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Root<USER> root)
	{
		List<Order> orders = new ArrayList<>();
		for(JeeslCqOrdering c : ListUtils.emptyIfNull(query.getCqOrderings()))
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