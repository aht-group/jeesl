package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.io.db.meta.JeeslDbMetaSnapshot;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;

public interface JeeslIoDbQuery<SYSTEM extends JeeslIoSsiSystem<?,?>,
								SNAP extends JeeslDbMetaSnapshot<SYSTEM,?,?,?,?>>
					extends JeeslCoreQuery,JeeslOrderingQuery
{	
//	List<CqId> getIds();
	List<String> getRootFetches();
	List<String> getCodeList();
	
	//CQ
//	List<CqOrdering> getOrderings();
	
	List<SYSTEM> getSystems();
	List<SNAP> getSnapshots();
}