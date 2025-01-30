package org.jeesl.model.json.system.security.oauth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonAccessToken implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonAccessToken() {}
	
	@JsonProperty("access_token")
	private String tokenAccess;
	public String getTokenAccess() {return tokenAccess;}
	public void setTokenAccess(String tokenAccess) {this.tokenAccess = tokenAccess;}

	@JsonProperty("token_type")
	private String tokenType;
	public String getTokenType() {return tokenType;}
	public void setTokenType(String tokenType) {this.tokenType = tokenType;}
	
	@JsonProperty("scope")
	private String scope;
	public String getScope() {return scope;}
	public void setScope(String scope) {this.scope = scope;}

	@JsonProperty("expires_in")
	private Integer expiresIn;
	public Integer getExpiresIn() {return expiresIn;}
	public void setExpiresIn(Integer expiresIn) {this.expiresIn = expiresIn;}

	@JsonProperty("refresh_token")
	private String tokenRefresh;
	public String getTokenRefresh() {return tokenRefresh;}
	public void setTokenRefresh(String tokenRefresh) {this.tokenRefresh = tokenRefresh;}


	@JsonProperty("id_token")
	private String tokenId;
	public String getTokenId() {return tokenId;}
	public void setTokenId(String tokenId) {this.tokenId = tokenId;}

}