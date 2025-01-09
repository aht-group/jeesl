package org.jeesl.interfaces.util.query.system;

import java.util.List;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;

public interface JeeslSecurityQuery <C extends JeeslSecurityCategory<?,?>,
									R extends JeeslSecurityRole<?,?,C,?,U,?>,
									U extends JeeslSecurityUsecase<?,?,C,R,?,?>,
									A extends JeeslSecurityAction<?,?,R,?,U,?>,
									CTX extends JeeslSecurityContext<?,?>,
									USER extends JeeslUser<R>>
					extends JeeslCoreQuery,JeeslOrderingQuery
{	
//	void x();
	
	List<String> getRootFetches();
	
	
	List<USER> getUsers();
	List<C> getSecurityCategory();
	List<R> getSecurityRole();
	List<A> getSecurityAction();
	List<CTX> getSecurityContext();
}