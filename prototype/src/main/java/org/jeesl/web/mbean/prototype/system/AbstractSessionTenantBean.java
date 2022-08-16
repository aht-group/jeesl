package org.jeesl.web.mbean.prototype.system;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jeesl.interfaces.bean.system.JeeslSessionTenantBean;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSessionTenantBean <CTX extends JeeslSecurityContext<?,?>> implements Serializable, JeeslSessionTenantBean<CTX>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSessionTenantBean.class);
	
	protected CTX context; @Override public CTX getContext() {return context;}
	
	protected boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	protected boolean hoverMainMenuSubItems; @Override public boolean isHoverMainMenuSubItems() {return hoverMainMenuSubItems;}
	
	protected void postConstructSessionTenant()
	{
		FacesContext fc = FacesContext.getCurrentInstance();
	
		if(debugOnInfo)
		{
			if(fc==null) {logger.warn(FacesContext.class.getSimpleName()+" is null");}
			if(fc.getExternalContext()==null) {logger.warn(ExternalContext.class.getSimpleName()+" is null");}
			
			logger.info("getRequestContextPath: "+FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath());
//			logger.info("getApplicationContextPath: "+FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath());
			logger.info("getRequestScheme: "+FacesContext.getCurrentInstance().getExternalContext().getRequestScheme());
			logger.info("getRequestPathInfo: "+FacesContext.getCurrentInstance().getExternalContext().getRequestPathInfo());
			logger.info("getRequestServerName: "+FacesContext.getCurrentInstance().getExternalContext().getRequestServerName());
			logger.info("getRequestServletPath: "+FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath());
		}
	}
}