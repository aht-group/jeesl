package org.jeesl.factory.json.io.ssi.core;

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
		json.setHost(ejb.getHost());
		json.setPort(ejb.getPort());
		
		json.setUser(ejb.getUser());
		json.setPassword(ejb.getPwd());
		
		return json;
	}
}