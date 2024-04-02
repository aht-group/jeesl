package org.jeesl.interfaces.util.query.system;

import java.util.List;

import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.model.ejb.io.db.CqOrdering;

public interface JeeslSecurityQuery <CTX extends JeeslSecurityContext<?,?>>
					extends JeeslCoreQuery
{	
//	List<CqId> getIds();
	List<String> getRootFetches();
//	List<String> getCodeList();
	
	List<CqOrdering> getOrderings();
	
	List<CTX> getSecurityContext();
}