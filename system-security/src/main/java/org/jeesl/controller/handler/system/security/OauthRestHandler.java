package org.jeesl.controller.handler.system.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.utils.URIBuilder;
import org.exlp.util.io.JsonUtil;
import org.jeesl.api.rest.i.system.security.JeeslOauthRestInterface;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.json.system.security.JeeslRestBasicAuthenticator;
import org.jeesl.interfaces.controller.handler.system.security.JeeslOauthConfigProvider;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.security.oauth.JeeslSecurityOauthKeyType;
import org.jeesl.model.ejb.system.security.oauth.SecurityOauthKey;
import org.jeesl.model.ejb.system.security.oauth.SecurityOauthKeyType;
import org.jeesl.model.ejb.system.security.oauth.SecurityOauthToken;
import org.jeesl.model.json.io.ssi.mobile.JsonLogin;
import org.jeesl.model.json.system.security.oauth.JsonAccessToken;
import org.jeesl.model.json.system.security.oauth.JsonOauthConfig;
import org.jeesl.model.json.system.security.oauth.JsonOauthUser;
import org.jeesl.model.json.system.security.oauth.JsonWebKey;
import org.jeesl.model.json.system.security.oauth.JsonWebKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

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
		logger.info("Requesting CONFIG");
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
	
	@Override public JsonWebKeys jwks()
	{
		logger.info("Requesting JWKS");
		JsonWebKeys json = new JsonWebKeys();
		json.setKeys(new ArrayList<>());
		
		List<SecurityOauthKey> keys = facade.allVisible(SecurityOauthKey.class);
		logger.info(SecurityOauthKey.class.getSimpleName()+" "+keys.size());
		
		if(keys.isEmpty()) {keys.add(this.toDefaultToken());}
		
		for(SecurityOauthKey ejb : keys)
		{
			try
			{
				RSAKey rsa = RSAKey.parse(ejb.getJson());
				
				JsonWebKey key = JsonUtil.read(JsonWebKey.class,rsa.toPublicJWK().toJSONString());
				key.setUse("sig");
				key.setAlgorithm("RS256");
				
				json.getKeys().add(key);
				
			}
			catch (ParseException | IOException e) {e.printStackTrace();}
		}
		JsonUtil.info(json);
		return json;
	}
	
	private SecurityOauthKey toDefaultToken()
	{
		
		List<SecurityOauthKey> keys = facade.allVisible(SecurityOauthKey.class);
		if(!keys.isEmpty()) {return keys.get(0);}
		try
		{
			SecurityOauthKey ejb = new SecurityOauthKey();
			ejb.setCode(UUID.randomUUID().toString());
	            
			RSAKey rsa = new RSAKeyGenerator(2048).keyID(ejb.getCode()).generate();
			
            SecurityOauthKeyType type = facade.fByEnum(SecurityOauthKeyType.class,JeeslSecurityOauthKeyType.Code.rsa);
           
            ejb.setCode("1");
            ejb.setJson(rsa.toJSONString());
            ejb.setType(type);
            ejb.setRecord(LocalDateTime.now());
            ejb.setVisible(true);
            ejb.setName("Default");
            ejb.setJson(rsa.toJSONString());
            ejb = facade.save(ejb);
            
            return ejb;
            
		}
		catch (JOSEException | JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		return null;
	}
	
	@Override public JsonAccessToken token(String httpAuth, String grantType, String code, String redirectUri, String clientId, String clientSecret)
	{
		logger.info("Requesting TOKEN");
		
		if(ObjectUtils.isNotEmpty(httpAuth) && ObjectUtils.allNull(clientId,clientSecret))
		{
			JsonLogin login = JeeslRestBasicAuthenticator.decode(httpAuth);
			clientId = login.getUsername();
			clientSecret = login.getPassword();
		}
		
		logger.info("\thttpAuth:"+httpAuth);
		logger.info("\tgrantType:"+grantType);
		logger.info("\tcode:"+code);
		logger.info("\tredirectUri:"+redirectUri);
		logger.info("\tclientId:"+clientId);
		logger.info("\tclientSecret:"+clientSecret);
		
		String accessToken = generateAccessToken(clientId);
		 
		SecurityOauthToken token = null;
		try
		{
			token = facade.fByCode(SecurityOauthToken.class, code);
			
			SecurityOauthToken next = new SecurityOauthToken();
			next.setCode(accessToken);
			next.setUser(token.getUser());
			next = facade.save(next);
		}
		catch (JeeslNotFoundException | JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
		

	    String idToken = generateIdToken(clientId,token);

		JsonAccessToken json = new JsonAccessToken();
		json.setTokenAccess(accessToken);
		json.setTokenId(idToken);
		json.setTokenType("Bearer");
		json.setExpiresIn(3600);
		json.setScope("openid profile email");
		json.setTokenRefresh("tr");
		
		JsonUtil.info(json);
		return json;
	}
	
	private String generateAccessToken(String clientId) {return UUID.randomUUID().toString();}
	private String generateIdToken(String clientId, SecurityOauthToken token)
	{
	    try
	    {
	        JWTClaimsSet claims = new JWTClaimsSet.Builder()
	            .subject(token.getUser().getEmail().toLowerCase())
	            .issuer(cp.getIssuer())
	            .audience(clientId)
	            .issueTime(new Date())
	            .expirationTime(new Date(new Date().getTime() + 3600 * 1000))
	            .claim("nonce", token.getNonce())
	            .build();

	        SecurityOauthKey ejb = this.toDefaultToken();
	        RSAKey rsa = RSAKey.parse(ejb.getJson());
				
	        JWSSigner signer = new RSASSASigner(rsa.toPrivateKey());
	        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
	        signedJWT.sign(signer);

	        return signedJWT.serialize();
	    }
	    catch (JOSEException | ParseException e) {throw new RuntimeException("Fehler beim Erstellen des ID-Tokens", e);}
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
	
	@Override
	public JsonOauthUser user(String httpAuth)
	{
		logger.info("Requesting USER");
		
		JsonLogin login = JeeslRestBasicAuthenticator.decode(httpAuth);
		JsonUtil.info(login);
		
		JsonOauthUser user = new JsonOauthUser();
		try
		{
			SecurityOauthToken token = facade.fByCode(SecurityOauthToken.class, login.getToken());
			
			user.setSubject(token.getUser().getEmail().toLowerCase());
			user.setEmail(token.getUser().getEmail());
			user.setEmailVerified(true);
			user.setFirstName(token.getUser().getFirstName());
			user.setLastName(token.getUser().getLastName());
			user.setName(user.getFirstName()+" "+user.getLastName());
			
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		JsonUtil.info(user);
		
		
		return user;
	}
}