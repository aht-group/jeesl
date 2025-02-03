package org.jeesl.web.rest.auth;

import org.jeesl.factory.json.system.security.JeeslRestBasicAuthenticator;
import org.jeesl.model.json.io.ssi.mobile.JsonLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JeeslRestSecurityInterceptor
{
	final static Logger logger = LoggerFactory.getLogger(JeeslRestSecurityInterceptor.class);
	
	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	public JeeslRestSecurityInterceptor()
	{
		debugOnInfo = false;
	}
	
	public boolean isBasicAuthentication(String authorizationHeader)
    {
    	return authorizationHeader.toUpperCase().startsWith(JeeslRestBasicAuthenticator.BASIC);
    }
    
    protected JsonLogin decodeBasicAuthentication(String authorizationHeader)
    {
    	return JeeslRestBasicAuthenticator.decode(authorizationHeader);
    }
}