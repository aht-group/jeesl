package org.jeesl.exception.ejb;

import java.io.Serializable;
import java.util.Date;

public class JeeslNotFoundException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;

	private boolean withDetails;
	private Date when;

	private String whereKey,whereDetail;
	private String whatKey,whatDetail;
	
	private Class<?> searchClass; public Class<?> getSearchClass() {return searchClass;} public JeeslNotFoundException searchClass(Class<?> c) {this.searchClass=c; return this;}

	public static JeeslNotFoundException instance(String msg) {return new JeeslNotFoundException(msg);}
	public static JeeslNotFoundException from(JeeslNotFoundRuntimeException e) {return JeeslNotFoundException.instance(e.getMessage()).searchClass(e.getSearchClass());}
	
	public JeeslNotFoundException(String s)
	{
		super(s);
		withDetails=false;
	}

	 public JeeslNotFoundException(String s, Throwable cause)
	 {
		 super(s, cause);
		 withDetails=false;
	 }

	public JeeslNotFoundException()
	{
		super("Something is not found, additional infos set in extended attributes of "+JeeslNotFoundException.class.getSimpleName());
		when = new Date();
		withDetails=true;
	}

	public String toHash()
	{
		return whereKey+"-"+whatKey;
	}

	public boolean isWithDetails() {return withDetails;}
	public Date getWhen() {return when;}

	public String getWhereKey() {return whereKey;}
	public void setWhereKey(String whereKey) {this.whereKey = whereKey;}

	public String getWhereDetail() {return whereDetail;}
	public void setWhereDetail(String whereDetail) {this.whereDetail = whereDetail;}

	public String getWhatKey() {return whatKey;}
	public void setWhatKey(String whatKey) {this.whatKey = whatKey;}

	public String getWhatDetail() {return whatDetail;}
	public void setWhatDetail(String whatDetail) {this.whatDetail = whatDetail;}
	
	@Override
	public String toString()
	{
		if(withDetails)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(whereKey).append(" ").append(whereDetail);
			sb.append(" ").append(whatKey).append(" ").append(whatDetail);
			return sb.toString();
		}
		return super.toString();
	}

}