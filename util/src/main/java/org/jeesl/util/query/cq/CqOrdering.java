package org.jeesl.util.query.cq;

import java.io.Serializable;

import org.jeesl.model.ejb.io.db.CqGraphFetch;
import org.jeesl.model.ejb.io.db.JeeslCqOrdering;

public class CqOrdering implements JeeslCqOrdering
{
	private static final long serialVersionUID = 1L;
	
	private final SortOrder order; @Override public SortOrder getOrder() {return order;}
	private String path; @Override public String getPath() {return path;}

	public static CqOrdering ascending(Serializable...path) {return new CqOrdering(SortOrder.ASCENDING,CqGraphFetch.path(path));}
	public static CqOrdering desending(Serializable...path) {return new CqOrdering(SortOrder.DESCENDING,CqGraphFetch.path(path));}
	public static CqOrdering unsorted(Serializable...path) {return new CqOrdering(SortOrder.UNSORTED,CqGraphFetch.path(path));}

	private CqOrdering(SortOrder order, String path)
	{
		this.order=order;
		this.path=path;
	}

	public static String path(Serializable...attributes) {return CqGraphFetch.path(attributes);}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(order.toString());
		sb.append(" ");
		sb.append(path);
		return sb.toString();
	}
	
	public String nyi(Class<?> c)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("NYI ");
		sb.append(this.toString());
		sb.append(" ").append(c.getName());
		return sb.toString();
	}
}