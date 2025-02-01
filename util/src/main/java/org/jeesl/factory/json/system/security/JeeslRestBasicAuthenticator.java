package org.jeesl.factory.json.system.security;

import org.apache.commons.codec.binary.Base64;
import org.jeesl.model.json.io.ssi.mobile.JsonLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRestBasicAuthenticator
{ 
	final static Logger logger = LoggerFactory.getLogger(JeeslRestBasicAuthenticator.class);

	public static final String BASIC = "BASIC ";	
	
	public static JsonLogin decode(String authorizationHeader) throws javax.ws.rs.NotAuthorizedException
	{
		if(authorizationHeader.toUpperCase().startsWith("BASIC "))
		{
			String base64 = authorizationHeader.substring("BASIC ".length());
			logger.trace("Base64: "+base64);
    		String token = new String(Base64.decodeBase64(base64));
        	logger.trace("Authorization: "+token);
        	
        	String[] splitted = token.split(":");
        	logger.trace("L: "+splitted.length);
        	
        	if(splitted.length<2) {throw new javax.ws.rs.NotAuthorizedException("The format of the decoded authorisation token ("+token+") is wrong. See https://www.ietf.org/rfc/rfc2617.txt");}
        	else if(splitted.length>2) {throw new javax.ws.rs.NotAuthorizedException("The character ':' in username/password is not allowed in decoded authorisation token ("+token+"). https://www.ietf.org/rfc/rfc2617.txt");}
        	else
        	{
        		JsonLogin login = new JsonLogin();
        		login.setUsername(splitted[0]);
        		login.setPassword(splitted[1]);
        		return login;
        	}
		}
		else {throw new javax.ws.rs.NotAuthorizedException("We only support BASIC authentication");}
	}
}