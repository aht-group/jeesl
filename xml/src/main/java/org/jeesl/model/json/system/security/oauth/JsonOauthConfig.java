package org.jeesl.model.json.system.security.oauth;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonOauthConfig implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonOauthConfig() {}
	
	@JsonProperty("issuer")
	private String issuer;
	public String getIssuer() {return issuer;}
	public void setIssuer(String issuer) {this.issuer = issuer;}
	
	@JsonProperty("authorization_endpoint")
	private String endpointAuth;
	public String getEndpointAuth() {return endpointAuth;}
	public void setEndpointAuth(String endpointAuth) {this.endpointAuth = endpointAuth;}
	
	@JsonProperty("token_endpoint")
	private String endpointToken;
	public String getEndpointToken() {return endpointToken;}
	public void setEndpointToken(String endpointToken) {this.endpointToken = endpointToken;}

	@JsonProperty("userinfo_endpoint")
	private String endpointUserInfo;
	public String getEndpointUserInfo() {return endpointUserInfo;}
	public void setEndpointUserInfo(String endpointUserInfo) {this.endpointUserInfo = endpointUserInfo;}
	
	@JsonProperty("jwks_uri")
	private String uriJwks;
	public String getUriJwks() {return uriJwks;}
	public void setUriJwks(String uriJwks) {this.uriJwks = uriJwks;}
	
	@JsonProperty("registration_endpoint")
	private String endpointRegistration;

	public String getEndpointRegistration() {
		return endpointRegistration;
	}
	public void setEndpointRegistration(String endpointRegistration) {
		this.endpointRegistration = endpointRegistration;
	}
	
	@JsonProperty("scopes_supported")
	private List<String> scopes;

	public List<String> getScopes() {
		return scopes;
	}
	public void setScopes(List<String> scopes) {
		this.scopes = scopes;
	}
	
	@JsonProperty("response_types_supported")
	private List<String> responses;

	public List<String> getResponses() {
		return responses;
	}
	public void setResponses(List<String> responses) {
		this.responses = responses;
	}
	
	@JsonProperty("grant_types_supported")
	private List<String> grants;

	public List<String> getGrants() {
		return grants;
	}
	public void setGrants(List<String> grants) {
		this.grants = grants;
	}

	@JsonProperty("subject_types_supported")
	private List<String> subjects;

	public List<String> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<String> subjects) {
		this.subjects = subjects;
	}
	
	@JsonProperty("id_token_signing_alg_values_supported")
	private List<String> tokenSignings;

	public List<String> getTokenSignings() {
		return tokenSignings;
	}
	public void setTokenSignings(List<String> tokenSignings) {
		this.tokenSignings = tokenSignings;
	}
	
	
	
	@JsonProperty("id_token_encryption_alg_values_supported")
	private List<String> tokenEncryptionAlgs;

	public List<String> getTokenEncryptionAlgs() {return tokenEncryptionAlgs;}
	public void setTokenEncryptionAlgs(List<String> tokenEncryptions) {
		this.tokenEncryptionAlgs = tokenEncryptions;
	}
	
	@JsonProperty("id_token_encryption_enc_values_supported")
	private List<String> tokenEncryptionValues;
	public List<String> getTokenEncryptionValues() {
		return tokenEncryptionValues;
	}
	public void setTokenEncryptionValues(List<String> tokenEncryptionValues) {
		this.tokenEncryptionValues = tokenEncryptionValues;
	}

	@JsonProperty("token_endpoint_auth_methods_supported")
	private List<String> endpointAuths;

	public List<String> getEndpointAuths() {
		return endpointAuths;
	}
	public void setEndpointAuths(List<String> endpointAuths) {
		this.endpointAuths = endpointAuths;}
	
	
	
	@JsonProperty("token_endpoint_auth_signing_alg_values_supported")
	private List<String> endpointSignings;

	public List<String> getEndpointSignings() {
		return endpointSignings;
	}
	public void setEndpointSignings(List<String> endpointSignings) {
		this.endpointSignings = endpointSignings;
	} 
	
	@JsonProperty("claims_parameter_supported")
	private Boolean supportedClaims;
	
	
	public Boolean getSupportedClaims() {
		return supportedClaims;
	}
	public void setSupportedClaims(Boolean supportedClaims) {
		this.supportedClaims = supportedClaims;
	}
	
	
	
	

	@JsonProperty("request_parameter_supported")
	private Boolean supportedRequest;
	
	public Boolean getSupportedRequest() {
		return supportedRequest;
	}
	public void setSupportedRequest(Boolean supportedRequest) {
		this.supportedRequest = supportedRequest;
	}

	@JsonProperty("request_uri_parameter_supported")
	private Boolean supportedUri;

	public Boolean getSupportedUri() {
		return supportedUri;
	}
	public void setSupportedUri(Boolean supportedUri) {
		this.supportedUri = supportedUri;
	}

}