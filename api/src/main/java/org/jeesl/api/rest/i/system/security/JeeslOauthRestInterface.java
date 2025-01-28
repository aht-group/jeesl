package org.jeesl.api.rest.i.system.security;

import org.jeesl.model.json.system.security.oauth.JsonAccessToken;
import org.jeesl.model.json.system.security.oauth.JsonOauthConfig;

public interface JeeslOauthRestInterface
{ 
	String dateTimePublic();
	JsonOauthConfig config();
	JsonAccessToken token(String httpAuth, String grantType, String code, String redirectUri, String clientId, String clientSecret);
}