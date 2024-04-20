package org.jeesl.interfaces.util.query.system;

import java.util.List;

import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.util.JeeslSecurityCategory;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;

public interface JeeslSecurityQuery <C extends JeeslSecurityCategory<?,?>,
									R extends JeeslSecurityRole<?,?,C,?,?,?>,
									CTX extends JeeslSecurityContext<?,?>>
					extends JeeslCoreQuery,JeeslOrderingQuery
{	
	List<String> getRootFetches();
//	List<CqOrdering> getOrderings();
	
	List<C> getSecurityCategory();
	List<R> getSecurityRole();
	List<CTX> getSecurityContext();
}