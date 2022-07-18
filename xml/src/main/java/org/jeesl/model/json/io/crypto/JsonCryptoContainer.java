package org.jeesl.model.json.io.crypto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="crypto")
public class JsonCryptoContainer implements Serializable
{
	public static final long serialVersionUID=1;
	
	@JsonProperty("iv")
	private String iv;
	public String getIv() {return iv;}
	public void setIv(String iv) {this.iv = iv;}
	
	@JsonProperty("hash")
	private String hash;
	public String getHash() {return hash;}
	public void setHash(String hash) {this.hash = hash;}
	
	@JsonProperty("datas")
	private List<JsonCryptoData> datas;
	public List<JsonCryptoData> getDatas() {return datas;}
	public void setDatas(List<JsonCryptoData> datas) {this.datas = datas;}
	
}