package org.jeesl.interfaces.model.system.security.user;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentityLogin;

public interface JeeslIdentity <R extends JeeslSecurityRole<?,?,?,V,U,A,USER>,
								V extends JeeslSecurityView<?,?,?,R,U,A>,
								U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
								A extends JeeslSecurityAction<?,?,R,V,U,?>,
								CTX extends JeeslSecurityContext<?,?>,
								USER extends JeeslUser<R>>
				extends JeeslIdentityLogin
{	
	USER getUser();
	void setUser(USER user);
	
	CTX getContext();
	void setContext(CTX context);
	
	boolean isLoggedIn();
	void setLoggedIn(boolean loggedIn);
	
	boolean hasUsecase(String usecaseCode);
	boolean hasView(String code);
	boolean hasView(V view);
	boolean hasRole(R role);
	boolean hasRole(String code);
	boolean hasAction(String code);
	
	int sizeAllowedUsecases();
	int sizeAllowedViews();
	int sizeAllowedRoles();
	int sizeAllowedActions();
	
	void allowView(V view);
	void allowRole(R role);
	void allowAction(A action);
	
	String getRoleCodeWithAccessToAllPages();
}