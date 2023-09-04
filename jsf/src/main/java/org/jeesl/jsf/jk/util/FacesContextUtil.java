package org.jeesl.jsf.jk.util;

import jakarta.servlet.http.HttpServletRequest;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;

public class FacesContextUtil
{
	final static Logger logger = LoggerFactory.getLogger(FacesContextUtil.class);

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

}