package org.jeesl.model.ejb.io.db;

import java.io.Serializable;

public class CqElOrdering implements Serializable
{
	private static final long serialVersionUID = 1L;

	public enum SortOrder {ASCENDING,DESCENDING,UNSORTED}
	
	private final SortOrder order; public final SortOrder getOrder() {return order;}
	private String path; public String getPath() {return path;}

	public static CqElOrdering ascending(Serializable...path) {return new CqElOrdering(SortOrder.ASCENDING,CqElOrdering.path(path));}
	public static CqElOrdering desending(Serializable...path) {return new CqElOrdering(SortOrder.DESCENDING,CqElOrdering.path(path));}
	public static CqElOrdering unsorted(Serializable...path) {return new CqElOrdering(SortOrder.UNSORTED,CqElOrdering.path(path));}

	private CqElOrdering(SortOrder order, String path)
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