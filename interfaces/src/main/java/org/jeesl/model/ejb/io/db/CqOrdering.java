package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public class CqOrdering implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum SortOrder {ASCENDING,DESCENDING,UNSORTED}
	
	private final SortOrder order; public final SortOrder getOrder() {return order;}
	private String path; public String getPath() {return path;}

	public static CqOrdering ascending(Serializable...path) {return new CqOrdering(SortOrder.ASCENDING,CqOrdering.path(path));}
	public static CqOrdering desending(Serializable...path) {return new CqOrdering(SortOrder.DESCENDING,CqOrdering.path(path));}
	public static CqOrdering unsorted(Serializable...path) {return new CqOrdering(SortOrder.UNSORTED,CqOrdering.path(path));}

	private CqOrdering(SortOrder order, String path)
	{
		this.order=order;
		this.path=path;
	}

	public static String path(Serializable...attributes)
	{
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<attributes.length;i++)
		{
			sb.append(attributes[i].toString());
			if(i<(attributes.length-1)) {sb.append(".");}
		}
		return sb.toString();
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(order.toString());
		sb.append(" ");
		sb.append(path);
		return sb.toString();
	}
}