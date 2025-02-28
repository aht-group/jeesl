package org.jeesl.controller.handler.system.security;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.utils.URIBuilder;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.jsf.jx.util.FacesContextUtil;
import org.jeesl.model.ejb.system.security.oauth.SecurityOauthToken;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OauthJsfHandler implements Serializable
{
	private static final long serialVersionUID = 1;

	final static Logger logger = LoggerFactory.getLogger(OauthJsfHandler.class);
	
	private JeeslFacade facade;
	
	public static OauthJsfHandler instance(JeeslFacade facade) {return new OauthJsfHandler(facade);}
	private OauthJsfHandler(JeeslFacade facade)
	{
		this.facade=facade;
	}
	
	public String redirect(Map<String,String> params, SecurityUser user) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException, URISyntaxException
	{
		String scope = null;
		String redirectUri = null;
		String state = null;
		String nonce = null;
		String responseType = null;
		String clinetId = null;
		
		for(String key : params.keySet())
		{
			logger.info(key+": "+params.get(key));
			
			if(key.equals("scope")) {scope = params.get("scope");}
			else if(key.equals("redirect_uri")) {redirectUri = params.get("redirect_uri");}
			else if(key.equals("state")) {state = params.get("state");}
			else if(key.equals("nonce")) {nonce = params.get("nonce");}
			else if(key.equals("response_type")) {responseType = params.get("response_type");}
			else if(key.equals("client_id")) {clinetId = params.get("client_id");}
		}
		
		SecurityOauthToken token = new SecurityOauthToken();
		token.setCode(UUID.randomUUID().toString());
		token.setNonce(nonce);
		token.setUser(user);
		token = facade.save(token);

		URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter("code", token.getCode());
        uriBuilder.addParameter("state", state);
		
		String redirect = uriBuilder.build().toString();
		logger.info("Redirecting to "+redirect);
		
		return redirect;
	}
}