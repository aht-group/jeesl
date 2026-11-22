package org.jeesl.util.query.json;

import org.jeesl.model.json.system.security.JsonSecurityRole;
import org.jeesl.model.json.system.security.JsonSecurityUser;

public class JsonSecurityQueryProvider
{	
	public static JsonSecurityRole collection()
	{
		JsonSecurityRole json = new JsonSecurityRole();
		json.setId(0l);
		json.setCode("");
		json.setLabel("");
		return json;
	}
	
	public static JsonSecurityUser user()
	{
		JsonSecurityUser json = new JsonSecurityUser();
		json.setId(0l);
		json.setFirstName("");
		json.setLastName("");
		json.setEmail("");
		return json;
	}
}