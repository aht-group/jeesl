package org.jeesl.api.facade.system;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslStaff;
import org.jeesl.interfaces.model.system.security.with.JeeslSecurityWithCategory;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

import net.sf.ahtutils.interfaces.model.util.UtilsStaffPool;

public interface JeeslSecurityFacade <L extends JeeslLang, D extends JeeslDescription,
										C extends JeeslSecurityCategory<L,D>,
										R extends JeeslSecurityRole<L,D,C,V,U,A,USER>,
										V extends JeeslSecurityView<L,D,C,R,U,A>,
										U extends JeeslSecurityUsecase<L,D,C,R,V,A>,
										A extends JeeslSecurityAction<L,D,R,V,U,AT>,
										AT extends JeeslSecurityTemplate<L,D,C>,
										M extends JeeslSecurityMenu<V,M>,
										USER extends JeeslUser<R>>
	extends JeeslFacade
{	
	
	R load(R role, boolean withUsers);
	V load(Class<V> cView, V view);
	U load(Class<U> cUsecase, U usecase);
	
	<E extends Enum<E>> C fSecurityCategory(JeeslSecurityCategory.Type type, E code);
	
	List<V> allViewsForUser(USER user);
	
	List<R> allRolesForUser(USER user);
	List<R> rolesForView(V view);
	List<R> rolesForView(Class<V> cView, Class<USER> cUser, V view, USER user);
	List<R> rolesForAction(Class<A> cAction, A action);
	List<R> rolesForAction(Class<A> cAction, Class<USER> cUser, A action, USER user);
	
	List<A> allActionsForUser(USER user);
	List<A> allActions(Class<R> cRole, List<R> roles);
	
	void grantRole(Class<USER> clUser, Class<R> clRole, USER user, R role, boolean grant);
	boolean hasRole(Class<USER> clUser, Class<R> clRole, USER user, R role);
		
	<WC extends JeeslSecurityWithCategory<C>> List<WC> allForCategory(Class<WC> clWc, Class<C> clC, String catCode) throws JeeslNotFoundException;
	
	<S extends UtilsStaffPool<L,D,C,R,V,U,A,AT,P,E,USER>, P extends EjbWithId, E extends EjbWithId> List<S> fStaffPool(Class<S> clStaff, P pool);
	
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> S fStaff(Class<S> cStaff, USER user, R role, D1 domain) throws JeeslNotFoundException;
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffU(Class<S> cStaff, USER user);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffR(Class<S> cStaff, R role);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffD(Class<S> cStaff, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffD(Class<S> cStaff, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUR(Class<S> cStaff, USER user, R role);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUD(Class<S> cStaff, USER user, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffUD(Class<S> cStaff, USER user, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, D1 domain);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffRD(Class<S> cStaff, R role, List<D1> domains);
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<S> fStaffURD(Class<S> cStaff, USER user, R role, List<D1> domains);
	
	<S extends JeeslStaff<R,USER,D1,D2>, D1 extends EjbWithId, D2 extends EjbWithId> List<D1> fDomains(Class<V> cView, Class<S> cStaff, USER user, V view);
}