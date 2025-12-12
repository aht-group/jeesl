package org.jeesl.model.json.io.ssi.mqtt;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonRootName(value="mqttCipher")
public class JsonMqttCipher implements Serializable
{
	public static final long serialVersionUID=1;

	public JsonMqttCipher() {}
	
	@JsonProperty("iv")
	private String iv;
	public String getIv() {return iv;}
	public void setIv(String iv) {this.iv = iv;}
	
	@JsonProperty("hash")
	private String hash;
	public String getHash() {return hash;}
	public void setHash(String hash) {this.hash = hash;}
	
	@JsonProperty("cipherText")
	private String cipherText;
	public String getCipherText() {return cipherText;}
	public void setCipherText(String cipherText) {this.cipherText = cipherText;}
	

	private List<JsonMqttMessage> messages;
	public List<JsonMqttMessage> getMessages() {return messages;}
	public void setMessages(List<JsonMqttMessage> messages) {this.messages = messages;}
	
}