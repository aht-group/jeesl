package org.jeesl.jsf.handler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.exlp.util.io.StringUtil;
import org.jeesl.exception.jsf.JeeslSessionInitialisationException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSessionContextHandler <CTX extends JeeslSecurityContext<?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslSessionContextHandler.class);
	
	private final JeeslFacade facade;
	private final Class<CTX> cCtx;
	
	private boolean debugOnInfo;
	
	public static <CTX extends JeeslSecurityContext<?,?>> JeeslSessionContextHandler<CTX> instance(Class<CTX> cCtx, JeeslFacade facade)
	{
		return new JeeslSessionContextHandler<>(cCtx,facade);
	}
	private JeeslSessionContextHandler(Class<CTX> cCtx, JeeslFacade facade)
	{
		this.cCtx=cCtx;
		this.facade=facade;
		this.debugOnInfo = false;
	}
	
	public JeeslSessionContextHandler<CTX> withDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo=debugOnInfo; return this;}
	
	public CTX jxDetermineContext()
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}
		javax.faces.context.FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
		if(Objects.nonNull(facesContext)) {logger.info(javax.faces.context.FacesContext.class.getSimpleName()+": "+facesContext.toString());}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(JeeslSessionContextHandler.class.getSimpleName()).append(" is throwing a ");
			sb.append(JeeslSessionInitialisationException.class.getSimpleName()).append(", then the SeesionTenantBean is newly created");
			logger.warn(javax.faces.context.FacesContext.class.getSimpleName()+" is null");
			logger.warn(sb.toString());
			throw new JeeslSessionInitialisationException(sb.toString());
		}
		
		javax.faces.context.ExternalContext externalContext = null;
		if(Objects.nonNull(facesContext))
		{
			externalContext = facesContext.getExternalContext();
			if(Objects.isNull(externalContext)) {logger.warn(javax.faces.context.ExternalContext.class.getSimpleName()+" is null");}
			else if(debugOnInfo) {logger.info(javax.faces.context.ExternalContext.class.getSimpleName()+": "+externalContext.toString());}
		}
		
		CTX securityContext = null;
		if(Objects.nonNull(externalContext))
		{
			if(debugOnInfo)
			{
				logger.info("RequestContextPath: "+externalContext.getRequestContextPath());
				logger.info("RequestScheme: "+externalContext.getRequestScheme());
				logger.info("RequestServerPort: "+externalContext.getRequestServerPort());
				logger.info("RequestPathInfo: "+externalContext.getRequestPathInfo());
				logger.info("RequestServerName: "+externalContext.getRequestServerName());
				logger.info("RequestServletPath: "+externalContext.getRequestServletPath());
			}	
			
			String serverName = javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
			
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
	public static <V extends JeeslSecurityView<?,?,?,?,?,?>> void jxInvalidateAndRedirect(V view)
	{
		logger.trace("Invalidating Session and redirecting to "+view.getCode()+" "+view.getUrlMapping());
		try
		{
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+view.getUrlMapping());
		}
		catch (IOException e)
		{

		}
	}
	
	
	
	public CTX jkDetermineContext()
	{
		if(debugOnInfo) {logger.info(StringUtil.stars());}
		jakarta.faces.context.FacesContext facesContext = jakarta.faces.context.FacesContext.getCurrentInstance();
		if(Objects.nonNull(facesContext)) {logger.info(jakarta.faces.context.FacesContext.class.getSimpleName()+": "+facesContext.toString());}
		else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(JeeslSessionContextHandler.class.getSimpleName()).append(" is throwing a ");
			sb.append(JeeslSessionInitialisationException.class.getSimpleName()).append(", then the SeesionTenantBean is newly created");
			logger.warn(jakarta.faces.context.FacesContext.class.getSimpleName()+" is null");
			logger.warn(sb.toString());
			throw new JeeslSessionInitialisationException(sb.toString());
		}
		
		jakarta.faces.context.ExternalContext externalContext = null;
		if(Objects.nonNull(facesContext))
		{
			externalContext = facesContext.getExternalContext();
			if(Objects.isNull(externalContext)) {logger.warn(jakarta.faces.context.ExternalContext.class.getSimpleName()+" is null");}
			else if(debugOnInfo) {logger.info(jakarta.faces.context.ExternalContext.class.getSimpleName()+": "+externalContext.toString());}
		}
		
		CTX securityContext = null;
		if(Objects.nonNull(externalContext))
		{
			if(debugOnInfo)
			{
				logger.info("RequestContextPath: "+externalContext.getRequestContextPath());
				logger.info("ApplicationContextPath: "+externalContext.getApplicationContextPath());
				logger.info("RequestScheme: "+externalContext.getRequestScheme());
				logger.info("RequestServerPort: "+externalContext.getRequestServerPort());
				logger.info("RequestPathInfo: "+externalContext.getRequestPathInfo());
				logger.info("RequestServerName: "+externalContext.getRequestServerName());
				logger.info("RequestServletPath: "+externalContext.getRequestServletPath());
			}	
			
			String serverName = jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequestServerName();
			
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
	public static <V extends JeeslSecurityView<?,?,?,?,?,?>> void jkInvalidateAndRedirect(V view)
	{
		logger.trace("Invalidating Session and redirecting to "+view.getCode()+" "+view.getUrlMapping());
		try
		{
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+view.getUrlMapping());
		}
		catch (IOException e)
		{

		}
	}
}