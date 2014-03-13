package net.sf.ahtutils.jsf.util;

import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;

public class FacesContextUtil
{
	public static String get(String key) throws UtilsNotFoundException
	{
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey(key))
		{
			return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
		}
		else
		{
			throw new UtilsNotFoundException("HTTP Request Paramater '"+key+"' not available");
		}
	}
	
	public static HttpServletRequest getHttpServletRequest(final FacesContext facesContext)
	{
		final Object request = facesContext.getExternalContext().getRequest();
		if (request instanceof javax.servlet.http.HttpServletRequest)
		{
		      return (HttpServletRequest) request;
		}
		else {return null;}
	}

	public static HttpSession getHttpSession(final FacesContext facesContext)
	{
		final HttpServletRequest httpServletRequest = getHttpServletRequest(facesContext);
		if (httpServletRequest != null)
		{
			return httpServletRequest.getSession();
		}
		else {return null;}
	}
	
	public static BeanManager lookBeanManager()
	{
		try
		{
			final Object obj = new InitialContext().lookup("java:comp/BeanManager");
			return (BeanManager) obj;
		}
		catch (final Exception e)
		{
			throw new IllegalStateException("Lookup bean manager", e);
		}
	}
}