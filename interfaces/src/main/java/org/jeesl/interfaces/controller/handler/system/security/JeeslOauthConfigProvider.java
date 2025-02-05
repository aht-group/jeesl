package org.jeesl.interfaces.controller.handler.system.security;

import java.io.Serializable;

public interface JeeslOauthConfigProvider extends Serializable
{
	String getIssuer();
	String getEndpointAuth();
	String getEndpointToken();
	String getEndpointUserInfo();
	String getUriJwks();
	String getEndpointRegistration();
}