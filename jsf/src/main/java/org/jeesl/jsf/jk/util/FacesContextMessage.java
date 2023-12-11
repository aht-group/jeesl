package org.jeesl.jsf.jk.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;

public class FacesContextMessage
{
	public static FacesContextMessage intance() {return new FacesContextMessage();}
	private FacesContextMessage()
	{
		
	}
	
	public void info(String summary, String detail) {info(null,summary, detail);}
	public void info(String id, String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_INFO,summary,detail);
	}
	
	public void warn(String summary, String detail) {warn(null,summary, detail);}
	public void warn(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_WARN,summary,detail);
	}
	
	public void error(String summary, String detail) {error(null,summary, detail);}
	public void error(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_ERROR,summary,detail);
	}
	
	public void fatal(String summary, String detail) {fatal(null,summary, detail);}
	public void fatal(String id,String summary, String detail)
	{
		addMessage(id, FacesMessage.SEVERITY_FATAL,summary,detail);
	}
	
	public void addMessage(String id, Severity severity, String summary, String detail) {addMessage(id,new FacesMessage(severity,summary,detail));}
	public void addMessage(FacesMessage message) {addMessage(null, message);}
	public void addMessage(String id, FacesMessage message) {FacesContext.getCurrentInstance().addMessage(id, message);}
}