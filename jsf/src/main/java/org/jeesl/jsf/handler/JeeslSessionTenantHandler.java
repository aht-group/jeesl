package org.jeesl.jsf.handler;

import java.util.List;
import java.util.Objects;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class JeeslSessionTenantHandler <CTX extends JeeslSecurityContext<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSessionTenantHandler.class);
	
	private final JeeslFacade facade;
	private final Class<CTX> cCtx;
	
	private boolean debugOnInfo;
	
	public static <CTX extends JeeslSecurityContext<?,?>> JeeslSessionTenantHandler<CTX> instance(Class<CTX> cCtx, JeeslFacade facade)
	{
		return new JeeslSessionTenantHandler<>(cCtx,facade);
	}
	private JeeslSessionTenantHandler(Class<CTX> cCtx, JeeslFacade facade)
	{
		this.cCtx=cCtx;
		this.facade=facade;
		this.debugOnInfo = false;
	}
	
	public JeeslSessionTenantHandler<CTX> withDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo=debugOnInfo; return this;}
	
	public CTX determineContext()
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if(Objects.nonNull(facesContext)) {logger.info(FacesContext.class.getSimpleName()+": "+facesContext.toString());}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(JeeslSessionTenantHandler.class.getSimpleName()).append(" is throwing a ");
			sb.append(RuntimeException.class.getSimpleName()).append(", then the SeesionTenantBean is newly created");
			logger.warn(FacesContext.class.getSimpleName()+" is null");
			logger.warn(sb.toString());
			throw new RuntimeException(sb.toString());
		}
		
		ExternalContext externalContext = null;
		if(Objects.nonNull(facesContext))
		{
			externalContext = facesContext.getExternalContext();
			if(Objects.isNull(externalContext)) {logger.warn(ExternalContext.class.getSimpleName()+" is null");}
			else if(debugOnInfo) {logger.info(ExternalContext.class.getSimpleName()+": "+externalContext.toString());}
		}
		
		CTX securityContext = null;
		if(Objects.nonNull(externalContext))
		{
			logger.info("RequestContextPath: "+externalContext.getRequestContextPath());
			logger.info("ApplicationContextPath: "+externalContext.getApplicationContextPath());
			logger.info("RequestScheme: "+externalContext.getRequestScheme());
			logger.info("RequestServerPort: "+externalContext.getRequestServerPort());
			logger.info("RequestPathInfo: "+externalContext.getRequestPathInfo());
			logger.info("RequestServerName: "+externalContext.getRequestServerName());
			logger.info("RequestServletPath: "+externalContext.getRequestServletPath());
			
			String serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
			
			List<CTX> list = facade.allOrderedPosition(cCtx);
			for(CTX c : list)
			{
				if(c.getServerName()!=null && c.getServerName().equals(serverName)) {securityContext = c; break;}
			}
			
			if(!list.isEmpty() && Objects.isNull(securityContext))
			{
				securityContext = list.get(0);
				logger.warn("Setting default context: "+securityContext.toString());
			}
			else
			{
				logger.info("Using "+cCtx.getName()+" "+securityContext.getCode());
			}
		}
		
		return securityContext;
	}
}
