package org.jeesl.jsf.jx.util;

import java.util.Enumeration;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacesContextUtil
{
	final static Logger logger = LoggerFactory.getLogger(FacesContextUtil.class);

	public static <E extends Enum<E>> String get(E key) throws JeeslNotFoundException {return FacesContextUtil.get(key.toString());}
	public static String get(String key) throws JeeslNotFoundException
	{
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey(key))
		{
			return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
		}
		else
		{
			throw new JeeslNotFoundException("HTTP Request Paramater '"+key+"' not available");
		}
	}


	public static String url()
	{
		FacesContext ctx = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
		return request.getRequestURI().substring(request.getContextPath().length());
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

	public static String evalAsString(String pathExpression)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
		ELContext elContext = context.getELContext();
		ValueExpression vex = expressionFactory.createValueExpression(elContext, pathExpression, String.class);
		String result = (String) vex.getValue(elContext);
		return result;
	}
	
	public static Cookie cookie(String name) throws JeeslNotFoundException
	{
		Map<String,Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
		if(!cookies.containsKey(name)) {throw new JeeslNotFoundException("No cookie for "+name);}
		return (Cookie) cookies.get(name);
	}
	
	public static void debug()
	{
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		Enumeration<String> attrs = req.getAttributeNames();
		while (attrs.hasMoreElements())
		{
		    String name = attrs.nextElement();
		    logger.warn("ATTR: " + name + " = " + req.getAttribute(name));
		}
		
		Enumeration<String> params = req.getParameterNames();
		while (params.hasMoreElements())
		{
		    String name = params.nextElement();
		    logger.warn("PARAM: " + name + " = " + req.getParameter(name));
		}
	}
}