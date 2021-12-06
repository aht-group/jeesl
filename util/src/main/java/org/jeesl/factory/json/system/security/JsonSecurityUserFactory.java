package org.jeesl.factory.json.system.security;

import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.json.system.security.JsonSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSecurityUserFactory<U extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSecurityUserFactory.class);
	
	public static <U extends JeeslSimpleUser> JsonSecurityUserFactory<U> instance() {return new JsonSecurityUserFactory<>();}
	
	private JsonSecurityUserFactory()
	{

	}
	
	public static JsonSecurityUser build() {return new JsonSecurityUser();}
	
	public JsonSecurityUser build(U user)
	{
		JsonSecurityUser json = build();
		json.setEmail(user.getEmail());
		
		
		return json;
	}
}