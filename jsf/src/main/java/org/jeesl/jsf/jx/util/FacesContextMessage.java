package org.jeesl.jsf.jx.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class FacesContextMessage
{
	
	public static void info(String summary, String detail) {info(null,summary, detail);}
	public static void info(String id, String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_INFO,summary,detail);
	}
	
	public static void warn(String summary, String detail) {warn(null,summary, detail);}
	public static void warn(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_WARN,summary,detail);
	}
	
	public static void error(String summary, String detail) {error(null,summary, detail);}
	public static void error(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_ERROR,summary,detail);
	}
	
	public static void fatal(String summary, String detail) {fatal(null,summary, detail);}
	public static void fatal(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_FATAL,summary,detail);
	}
	
	public static void addMessage(String id, Severity severity, String summary, String detail)
	{  
		FacesMessage fm = new FacesMessage(severity,summary,detail);
		addMessage(id,fm);
	}
	
	public static void addMessage(FacesMessage message)
	{  
		addMessage(null, message);  
	}
	public static void addMessage(String id, FacesMessage message)
	{  
		FacesContext.getCurrentInstance().addMessage(id, message);  
	}
}