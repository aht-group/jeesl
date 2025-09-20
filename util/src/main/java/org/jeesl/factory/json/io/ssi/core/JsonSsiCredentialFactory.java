package org.jeesl.factory.json.io.ssi.core;

import org.apache.commons.lang3.ObjectUtils;
import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;

public class JsonSsiCredentialFactory
{
	private JsonSsiCredential json;
	
	public static JsonSsiCredentialFactory instance() {return new JsonSsiCredentialFactory();}
	private JsonSsiCredentialFactory()
	{
		
	}
	
	public JsonSsiCredentialFactory fluent() {json = new JsonSsiCredential(); return this;}
	public JsonSsiCredentialFactory code(String code) {json.setCode(code); return this;}
	public JsonSsiCredentialFactory url(String url) {json.setUrl(url); return this;}
	public JsonSsiCredentialFactory user(String user) {json.setUser(user); return this;}
	public JsonSsiCredentialFactory password(String password) {json.setPassword(password); return this;}
	public JsonSsiCredential json() {return json;}
	
	public static JsonSsiCredential build() {return new JsonSsiCredential();}
	public static JsonSsiCredential build(String code) {JsonSsiCredential json = JsonSsiCredentialFactory.build(); json.setCode(code); return json;}
	public static JsonSsiCredential build(String user, String pwd){return build(null,null,user,pwd);}
	public static JsonSsiCredential build(String host, String url, String user, String pwd)
	{
		JsonSsiCredential json = new JsonSsiCredential();
		json.setUser(user);
		json.setPassword(pwd);
		json.setUrl(url);
		json.setHost(host);
		return json;
	}
	
	public static <SYSTEM extends JeeslIoSsiSystem<?,?>> JsonSsiCredential build(JeeslIoSsiCredential<SYSTEM> ejb)
	{
		JsonSsiCredential json = new JsonSsiCredential();
		json.setCode(ejb.getCode());
		if(ObjectUtils.isNotEmpty(ejb.getToken())) {json.setHost(ejb.getHost());}
		json.setPort(ejb.getPort());
		json.setUrl(ejb.getUrl());
		
		json.setUser(ejb.getUser());
		json.setPassword(ejb.getPwd());
		if(ObjectUtils.isNotEmpty(ejb.getToken())) {json.setToken(ejb.getToken());}
		
		return json;
	}
	
	public static <MQTT extends Enum<MQTT>>  JsonSsiCredential build(Configuration config, MQTT mqtt)
	{
		JsonSsiCredential json = JsonSsiCredentialFactory.build();
		
		String host = config.getString("net.mqtt."+mqtt.toString()+".host");
		Integer port = config.getInt("net.mqtt."+mqtt.toString()+".port");
		String scheme = null; if(port==8883) {scheme = "ssl";} else {scheme = "tcp";}
				
		json.setUrl(scheme+"://"+host+":"+port);
		json.setUser(config.getString("net.mqtt."+mqtt.toString()+".user"));
		json.setPassword(config.getString("net.mqtt."+mqtt.toString()+".password"));
		
		return json;
	}
}