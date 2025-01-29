package org.jeesl.controller.handler.system.security;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.http.client.utils.URIBuilder;
import org.jeesl.api.rest.i.system.security.JeeslOauthRestInterface;
import org.jeesl.interfaces.controller.handler.system.security.JeeslOauthConfigProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.json.system.security.oauth.JsonAccessToken;
import org.jeesl.model.json.system.security.oauth.JsonOauthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OauthRestHandler implements JeeslOauthRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(OauthRestHandler.class);
	
	private final JeeslFacade facade;
	private JeeslOauthConfigProvider cp;
	
	public static OauthRestHandler instance(JeeslFacade facade) {return new OauthRestHandler(facade);}
	private OauthRestHandler(JeeslFacade facade)
	{
		this.facade=facade;
	}
	
	public OauthRestHandler config(JeeslOauthConfigProvider configOauth) {this.cp=configOauth; return this;}
	
	@Override public String dateTimePublic()
	{
		return LocalDateTime.now().toString();
	}
	
	@Override public JsonOauthConfig config()
	{
		if(Objects.isNull(cp)) {throw new IllegalStateException("The OAuth Configuration Provider needs to be set.");}
		// https://help.akana.com/content/current/cm/api_oauth/oauth_discovery/m_oauth_getOpenIdConnectWellknownConfiguration.htm
		
		JsonOauthConfig json = new JsonOauthConfig();
		json.setIssuer(cp.getIssuer());
		json.setEndpointAuth(cp.getEndpointAuth());
		json.setEndpointToken(cp.getEndpointToken());
		json.setEndpointUserInfo(cp.getEndpointUserInfo());
		json.setUriJwks(cp.getUriJwks());
		json.setEndpointRegistration(cp.getEndpointRegistration());
		
		json.setScopes(new ArrayList<>());
		json.getScopes().add("READ");
		json.getScopes().add("WRITE");
		json.getScopes().add("DELETE");
		json.getScopes().add("openid");
		json.getScopes().add("scope");
		json.getScopes().add("profile");
		json.getScopes().add("email");
		json.getScopes().add("address");
		json.getScopes().add("phone");
		
		json.setResponses(new ArrayList<>());
		json.getResponses().add("code");
		json.getResponses().add("code id_token");
		json.getResponses().add("code token");
		json.getResponses().add("code id_token token");
		json.getResponses().add("token");
		json.getResponses().add("id_token");
		json.getResponses().add("id_token token\\");
		
		json.setGrants(new ArrayList<>());
		json.getGrants().add("authorization_code");
		json.getGrants().add("implicit");
		json.getGrants().add("password");
		json.getGrants().add("client_credentials");
		json.getGrants().add("urn:ietf:params:oauth:grant-type:jwt-bearer");

		json.setSubjects(new ArrayList<>());
		json.getSubjects().add("public");
		
		json.setTokenSignings(new ArrayList<>());
		json.getTokenSignings().add("HS256");
		json.getTokenSignings().add("HS384");
		json.getTokenSignings().add("HS512");
		json.getTokenSignings().add("RS256");
		json.getTokenSignings().add("RS384");
		json.getTokenSignings().add("RS512");
		json.getTokenSignings().add("ES256");
		json.getTokenSignings().add("ES384");
		json.getTokenSignings().add("ES512");
		json.getTokenSignings().add("PS256");
		json.getTokenSignings().add("PS384");
		json.getTokenSignings().add("PS512");
		
		json.setTokenEncryptionAlgs(new ArrayList<>());
		json.getTokenEncryptionAlgs().add("RSA1_5");
		json.getTokenEncryptionAlgs().add("RSA-OAEP");
		json.getTokenEncryptionAlgs().add("RSA-OAEP-256");
		json.getTokenEncryptionAlgs().add("A128KW");
		json.getTokenEncryptionAlgs().add("A192KW");
		json.getTokenEncryptionAlgs().add("A256KW");
		json.getTokenEncryptionAlgs().add("A128GCMKW");
		json.getTokenEncryptionAlgs().add("A192GCMKW");
		json.getTokenEncryptionAlgs().add("A256GCMKW");
		json.getTokenEncryptionAlgs().add("dir");
		
		json.setTokenEncryptionValues(new ArrayList<>());
		json.getTokenEncryptionValues().add("A128CBC-HS256");
		json.getTokenEncryptionValues().add("A192CBC-HS384");
		json.getTokenEncryptionValues().add("A256CBC-HS512");
		json.getTokenEncryptionValues().add("A128GCM");
		json.getTokenEncryptionValues().add("A192GCM");
		json.getTokenEncryptionValues().add("A256GCM");
		
		json.setEndpointAuths(new ArrayList<>());
		json.getEndpointAuths().add("client_secret_post");
		json.getEndpointAuths().add("client_secret_basic");
		json.getEndpointAuths().add("client_secret_jwt");
		json.getEndpointAuths().add("private_key_jwt");

		json.setEndpointSignings(new ArrayList<>());
		json.getEndpointSignings().add("HS256");
		json.getEndpointSignings().add("RS256");
		
		json.setSupportedClaims(false);
		json.setSupportedRequest(false);
		json.setSupportedUri(false);
		
		return json;
	}
	
	@Override public JsonAccessToken token(String httpAuth, String grantType, String code, String redirectUri, String clientId, String clientSecret)
	{
		logger.info("Token");
		logger.info("\thttpAuth:"+httpAuth);
		logger.info("\tgrantType:"+grantType);
		logger.info("\tcode:"+code);
		logger.info("\tredirectUri:"+redirectUri);
		logger.info("\tclientId:"+clientId);
		logger.info("\tclientSecret:"+clientSecret);
		
		JsonAccessToken json = new JsonAccessToken();
		json.setTokenAccess("ta");
		json.setTokenType("Bearer");
		json.setExpiresIn(3600);
		json.setScope("openid profile email");
		json.setTokenRefresh("tr");
		json.setTokenId("tid");
		
		return json;
	}
	
	public static String handleAuthorizationEndpointRedirect(String redirectUri, String state, String authorizationCode) throws URISyntaxException
	{
		URIBuilder uriBuilder = new URIBuilder(redirectUri);
        uriBuilder.addParameter("code", authorizationCode);
        uriBuilder.addParameter("state", state);
        uriBuilder.build().toString();
        
		StringBuilder sb = new StringBuilder();
		try
		{
			sb.append(URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString()));
			sb.append("?code=").append(URLEncoder.encode(authorizationCode, StandardCharsets.UTF_8.toString()));
			sb.append("&state=").append(URLEncoder.encode(state, StandardCharsets.UTF_8.toString()));
		}
		catch (UnsupportedEncodingException e) {e.printStackTrace();}
		
		return sb.toString();
		
			// scope: openid profile email
			// response_type: code
			// redirect_uri: 
			// state: xxxx
			// nonce: yyyy
			// client_id: cid
	}
}