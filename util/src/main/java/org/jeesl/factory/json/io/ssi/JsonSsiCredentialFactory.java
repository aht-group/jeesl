package org.jeesl.factory.json.io.ssi;

import org.jeesl.model.json.system.io.ssi.JsonSsiCredential;

public class JsonSsiCredentialFactory
{
	public static JsonSsiCredential build(String user, String pwd){return build(null,user,pwd);}
	public static JsonSsiCredential build(String host, String user, String pwd)
	{
		JsonSsiCredential json = new JsonSsiCredential();
		json.setUser(user);
		json.setPassword(pwd);
		json.setHost("hmis.moh.gov.rw");
		return json;
	}
}