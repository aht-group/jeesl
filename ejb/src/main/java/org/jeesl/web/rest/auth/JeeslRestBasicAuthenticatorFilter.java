package org.jeesl.web.rest.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;

public class JeeslRestBasicAuthenticatorFilter implements ClientRequestFilter 
{
	private final String authHeader;
	    
	public JeeslRestBasicAuthenticatorFilter(String username, String password)
    {
        String credentials = username + ":" + password;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
    }

	@Override public void filter(ClientRequestContext requestContext) throws IOException
	{
		requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, authHeader);
	}
}