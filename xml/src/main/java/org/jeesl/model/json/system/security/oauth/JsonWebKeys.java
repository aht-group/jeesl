package org.jeesl.model.json.system.security.oauth;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="keys")
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonWebKeys implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonWebKeys() {}
	
	@JsonProperty("keys")
	private List<JsonWebKey> keys;
	public List<JsonWebKey> getKeys() {return keys;}
	public void setKeys(List<JsonWebKey> keys) {this.keys = keys;}
}