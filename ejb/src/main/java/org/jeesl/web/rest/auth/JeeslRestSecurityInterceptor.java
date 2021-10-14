package org.jeesl.web.rest.auth;

import org.jeesl.model.json.system.io.ssi.mobile.Login;
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
    
    protected Login decodeBasicAuthentication(String authorizationHeader)
    {
    	return JeeslRestBasicAuthenticator.decode(authorizationHeader);
    }
}