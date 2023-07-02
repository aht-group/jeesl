package org.jeesl.jsf.handler;

import java.util.Objects;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class JeeslSessionTenantHandler <CTX extends JeeslSecurityContext<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSessionTenantHandler.class);
	
	private boolean debugOnInfo;
	
	public static <CTX extends JeeslSecurityContext<?,?>> JeeslSessionTenantHandler<CTX> instance(Class<CTX> c)
	{
		return new JeeslSessionTenantHandler<>();
	}
	private JeeslSessionTenantHandler()
	{
		this.debugOnInfo = false;
	}
	
	public JeeslSessionTenantHandler<CTX> withDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo=debugOnInfo; return this;}
	
	public void determineContext()
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(Objects.isNull(facesContext)) {logger.warn(FacesContext.class.getSimpleName()+" is null");}
		else if(debugOnInfo) {logger.info(FacesContext.class.getSimpleName()+": "+facesContext.toString());}
		
		ExternalContext externalContext = facesContext.getExternalContext();
		if(Objects.isNull(externalContext)) {logger.warn(ExternalContext.class.getSimpleName()+" is null");}
		else if(debugOnInfo) {logger.info(ExternalContext.class.getSimpleName()+": "+externalContext.toString());}
		
		if(Objects.nonNull(externalContext))
		{
			logger.info("RequestContextPath: "+externalContext.getRequestContextPath());
			logger.info("ApplicationContextPath: "+externalContext.getApplicationContextPath());
			logger.info("RequestScheme: "+externalContext.getRequestScheme());
			logger.info("RequestServerPort: "+externalContext.getRequestServerPort());
			logger.info("RequestPathInfo: "+externalContext.getRequestPathInfo());
			logger.info("RequestServerName: "+externalContext.getRequestServerName());
			logger.info("equestServletPath: "+externalContext.getRequestServletPath());
		}
		
		
	}
}
