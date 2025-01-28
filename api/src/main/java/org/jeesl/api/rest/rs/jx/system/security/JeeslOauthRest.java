package org.jeesl.api.rest.rs.jx.system.security;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.system.security.JeeslOauthRestInterface;
import org.jeesl.model.json.system.security.oauth.JsonAccessToken;
import org.jeesl.model.json.system.security.oauth.JsonOauthConfig;

@Path("/oauth")
public interface JeeslOauthRest extends JeeslOauthRestInterface
{
	@GET @Path("/date") @Produces(MediaType.TEXT_PLAIN) 
	String dateTimePublic();
	
	@GET @Path("/.well-known/openid-configuration")
	@Produces(MediaType.APPLICATION_JSON) 
	JsonOauthConfig config();
	
	@POST @Path("/token")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	JsonAccessToken token(@HeaderParam("Authorization") String httpAuth,
					@FormParam("grant_type") String grantType,
		            @FormParam("code") String code,
		            @FormParam("redirect_uri") String redirectUri,
		            @FormParam("client_id") String clientId,
		            @FormParam("client_secret") String clientSecret
		            );
}