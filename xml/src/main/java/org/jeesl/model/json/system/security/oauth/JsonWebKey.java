package org.jeesl.model.json.system.security.oauth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="key")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebKey implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonWebKey() {}
	
	@JsonProperty("kty")
	private String type;
	public String getType() {return type;}
	public void setType(String type) {this.type = type;}

	@JsonProperty("kid")
	private String code;
	public String getCode() {return code;}
	public void setCode(String code) {this.code = code;}

	@JsonProperty("use")
	private String use;
	public String getUse() {return use;}
	public void setUse(String use) {this.use = use;}

	@JsonProperty("alg")
	private String algorithm;
	public String getAlgorithm() {return algorithm;}
	public void setAlgorithm(String algorithm) {this.algorithm = algorithm;}

	@JsonProperty("n")
	private String rsaModulus;
	public String getRsaModulus() {return rsaModulus;}
	public void setRsaModulus(String rsaModulus) {this.rsaModulus = rsaModulus;}

	@JsonProperty("e")
	private String rsaExponent;
	public String getRsaExponent() {return rsaExponent;}
	public void setRsaExponent(String rsaExponent) {this.rsaExponent = rsaExponent;}
}