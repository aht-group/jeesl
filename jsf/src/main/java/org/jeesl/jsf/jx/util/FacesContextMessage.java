package org.jeesl.jsf.jx.util;

import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacesContextMessage implements JeeslFacesContextMessage
{
	final static Logger logger = LoggerFactory.getLogger(FacesContextMessage.class);
	
	public static FacesContextMessage instance() {return new FacesContextMessage();}
	private FacesContextMessage()
	{
		
	}
	
	public void info(String summary, String detail) {info(null,summary, detail);}
	public <FID extends Enum<FID>> void info(FID id, String summary, String detail)
	{
		if(Objects.isNull(detail)) {addMessage(null, FacesMessage.SEVERITY_INFO,summary,detail);}
		else {addMessage(id.toString(), FacesMessage.SEVERITY_INFO,summary,detail);}
		
	}
	
	@Override public void warn(String summary, String detail) {warn(null,summary, detail);}
	@Override public void warn(String id, String summary, String detail) {addMessage(id, FacesMessage.SEVERITY_WARN,summary,detail);}
	
	public void error(String summary, String detail) {error(null,summary, detail);}
	public void error(String id,String summary, String detail) {addMessage(id, FacesMessage.SEVERITY_ERROR,summary,detail);}
	
//	private void fatal(String summary, String detail) {fatal(null,summary, detail);}
//	private void fatal(String id,String summary, String detail) {addMessage(id,FacesMessage.SEVERITY_FATAL,summary,detail);}
	
	private void addMessage(String id, Severity severity, String summary, String detail) {addMessage(id,new FacesMessage(severity,summary,detail));}
	private void addMessage(String id, FacesMessage message)
	{
		logger.info(FacesMessage.class.getSimpleName()+": "+message.getSeverity()+" ("+id+") "+message.getSummary()+": "+message.getSummary());
		FacesContext.getCurrentInstance().addMessage(id, message);
	}
}