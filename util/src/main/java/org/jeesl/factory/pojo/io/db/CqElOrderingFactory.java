package org.jeesl.factory.pojo.io.db;

import org.jeesl.util.query.cq.CqOrdering;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CqElOrderingFactory
{
	final static Logger logger = LoggerFactory.getLogger(CqElOrderingFactory.class);
	
	public static CqOrdering build(SortOrder sortOrder, String path)
	{	
		CqOrdering order = null;
		switch(sortOrder)
		{
			case ASCENDING: order = CqOrdering.ascending(path); break;
			case DESCENDING: order =  CqOrdering.desending(path); break;
			case UNSORTED: order =  CqOrdering.unsorted(path); break;
		}
		return order;
	}

}