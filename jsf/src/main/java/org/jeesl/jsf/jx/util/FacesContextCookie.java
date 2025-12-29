package org.jeesl.jsf.jx.util;

import java.util.Hashtable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacesContextCookie
{
	final static Logger logger = LoggerFactory.getLogger(FacesContextCookie.class);

	private static enum KEY {maxAge}
	
	// http://docs.oracle.com/javaee/6/api/javax/faces/context/ExternalContext.html#addResponseCookie(java.lang.String,%20java.lang.String,%20java.util.Map)
	public static void add(FacesContext ctx, Cookie cookie)
	{
		Map<String,Object> props = new Hashtable<String,Object>();
		props.put(KEY.maxAge.toString(), cookie.getMaxAge());
		
		ctx.getExternalContext().addResponseCookie(cookie.getName(), cookie.getValue(), props);
	}
}
