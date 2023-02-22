package org.jeesl.util.query.json;

import org.jeesl.model.json.system.security.JsonSecurityUser;

public class JsonSecurityQueryProvider
{	
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