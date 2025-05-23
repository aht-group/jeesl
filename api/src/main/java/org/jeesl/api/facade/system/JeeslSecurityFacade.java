package org.jeesl.api.facade.system;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.access.JeeslStaff;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.util.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.util.query.system.JeeslSecurityQuery;

public interface JeeslSecurityFacade <C extends JeeslSecurityCategory<?,?>,
										R extends JeeslSecurityRole<?,?,C,V,U,A>,
										V extends JeeslSecurityView<?,?,C,R,U,A>,
										U extends JeeslSecurityUsecase<?,?,C,R,V,A>,
										A extends JeeslSecurityAction<?,?,R,V,U,?>,
										CTX extends JeeslSecurityContext<?,?>,
										M extends JeeslSecurityMenu<?,V,CTX,M>,
										USER extends JeeslUser<R>>
	extends JeeslFacade
{	
	
	R load(R role);
	V load(Class<V> cView, V view);
	U load(Class<U> cUsecase, U usecase);
	
	<WC extends JeeslSecurityWithCategory<C>> List<WC> allForCategory(Class<WC> clWc, Class<C> clC, String catCode) throws JeeslNotFoundException;
	<E extends Enum<E>> C fSecurityCategory(JeeslSecurityCategory.Type type, E code);
	
	List<C> fSecurityCategories(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	List<R> fSecurityRoles(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	List<M> fSecurityMenus(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	List<A> fSecurityActions(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	List<USER> fSecurityUsers(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<U> fSecurityUsecases(JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query, Class<S> cStaff);
	
	List<V> allViewsForUser(USER user);
	List<USER> fUsers(R role);
	
	
	List<R> allRolesForUser(USER user);
	List<R> rolesForView(V view);
	List<R> rolesForView(Class<V> cView, Class<USER> cUser, V view, USER user);
	List<R> rolesForAction(Class<A> cAction, A action);
	List<R> rolesForAction(Class<A> cAction, Class<USER> cUser, A action, USER user);
	
	List<A> allActionsForUser(USER user);
	List<A> allActions(Class<R> cRole, List<R> roles);
	
	void grantRole(Class<USER> clUser, Class<R> clRole, USER user, R role, boolean grant);
	boolean hasSecurityRole(USER user, R role);
	
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> S fStaff(Class<S> cStaff, USER user, R role, D1 domain) throws JeeslNotFoundException;
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaff(Class<S> cStaff, JeeslSecurityQuery<C,R,V,U,A,CTX,USER> query);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffU(Class<S> cStaff, USER user);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffD(Class<S> cStaff, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffD(Class<S> cStaff, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUR(Class<S> cStaff, USER user, R role);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUR(Class<S> cStaff, USER user, List<R> roles);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUD(Class<S> cStaff, USER user, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUD(Class<S> cStaff, USER user, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, List<R> roles, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffURD(Class<S> cStaff, USER user, R role, List<D1> domains);

	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<D1> fDomains(Class<V> cView, Class<S> cStaff, USER user, V view);
}