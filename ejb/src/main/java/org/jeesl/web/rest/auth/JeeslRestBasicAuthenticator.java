package org.jeesl.web.rest.auth;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.jeesl.model.json.io.ssi.mobile.JsonLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslRestBasicAuthenticator
{ 
	final static Logger logger = LoggerFactory.getLogger(JeeslRestBasicAuthenticator.class);

	public static final String BASIC = "BASIC ";
//	private boolean debugOnInfo = true;
	
	private static JsonLogin decodeResteasy(HttpRequest request) throws UnauthorizedException
	{
		List<String> header = request.getHttpHeaders().getRequestHeader("Authorization");
    	if(header!=null && !header.isEmpty())
    	{
    		String auth = header.get(0);
    		return decode(auth);
    	}
    	else {throw new UnauthorizedException("No Authorization Header available");}
	}
	public static JsonLogin decode(String authorizationHeader)
	{
		if(authorizationHeader.toUpperCase().startsWith("BASIC "))
		{
			String base64 = authorizationHeader.substring("BASIC ".length());
			logger.trace("Base64: "+base64);
    		String token = new String(Base64.decodeBase64(base64));
        	logger.trace("Authorization: "+token);
        	
        	String[] splitted = token.split(":");
        	logger.trace("L: "+splitted.length);
        	
        	if(splitted.length<2) {throw new UnauthorizedException("The format of the decoded authorisation token ("+token+") is wrong. See https://www.ietf.org/rfc/rfc2617.txt");}
        	else if(splitted.length>2) {throw new UnauthorizedException("The character ':' in username/password is not allowed in decoded authorisation token ("+token+"). https://www.ietf.org/rfc/rfc2617.txt");}
        	else
        	{
        		JsonLogin login = new JsonLogin();
        		login.setUsername(splitted[0]);
        		login.setPassword(splitted[1]);
        		return login;
        	}
		}
		else {throw new UnauthorizedException("We only support BASIC authentication");}
	}
	
	public static JsonLogin decodeResteasy2(HttpRequest request) throws javax.ws.rs.NotAuthorizedException
	{
		List<String> header = request.getHttpHeaders().getRequestHeader("Authorization");
    	if(header!=null && !header.isEmpty())
    	{
    		String auth = header.get(0);
    		return decode2(auth);
    	}
    	else {throw new javax.ws.rs.NotAuthorizedException("No Authorization Header available");}
	}
	public static JsonLogin decode2(String authorizationHeader) throws javax.ws.rs.NotAuthorizedException
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