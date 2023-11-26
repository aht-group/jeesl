package org.jeesl.factory.pojo.system.io.db;

import org.jeesl.model.ejb.io.db.CqElOrdering;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqElOrderingFactory
{
	final static Logger logger = LoggerFactory.getLogger(CqElOrderingFactory.class);
	
	public static CqElOrdering build(SortOrder sortOrder, String path)
	{	
		CqElOrdering order = null;
		switch(sortOrder)
		{
			case ASCENDING: order = CqElOrdering.ascending(path); break;
			case DESCENDING: order =  CqElOrdering.desending(path); break;
			case UNSORTED: order =  CqElOrdering.unsorted(path); break;
		}
		return order;
	}

}