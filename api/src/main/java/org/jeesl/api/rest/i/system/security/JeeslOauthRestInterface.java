package org.jeesl.api.rest.i.system.security;

import org.jeesl.model.json.system.security.oauth.JsonAccessToken;
import org.jeesl.model.json.system.security.oauth.JsonOauthConfig;
import org.jeesl.model.json.system.security.oauth.JsonOauthUser;
import org.jeesl.model.json.system.security.oauth.JsonWebKeys;

public interface JeeslOauthRestInterface
{	
	String dateTimePublic();
	
	JsonOauthConfig config();
	JsonWebKeys jwks();
	
	JsonAccessToken token(String httpAuth, String grantType, String code, String redirectUri, String clientId, String clientSecret);
	JsonOauthUser user(String httpAuth);
}