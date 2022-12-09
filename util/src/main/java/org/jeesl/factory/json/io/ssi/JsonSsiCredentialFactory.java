package org.jeesl.factory.json.io.ssi;

import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiCredential;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.model.json.system.io.ssi.JsonSsiCredential;

public class JsonSsiCredentialFactory
{
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
		
		json.setUser(ejb.getUser());
		json.setPassword(ejb.getPwd());
		
		return json;
	}
}