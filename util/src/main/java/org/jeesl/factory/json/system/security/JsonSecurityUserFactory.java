package org.jeesl.factory.json.system.security;

import java.util.Objects;

import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.json.system.security.JsonSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSecurityUserFactory<U extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSecurityUserFactory.class);
	
	private final JsonSecurityUser q;
	
	public static <U extends JeeslSimpleUser> JsonSecurityUserFactory<U> instance(JsonSecurityUser q) {return new JsonSecurityUserFactory<>(q);}
	private JsonSecurityUserFactory(JsonSecurityUser q)
	{
		this.q=q;
	}
	
	public static JsonSecurityUser build() {return new JsonSecurityUser();}
	
	public JsonSecurityUser build(U user)
	{
		JsonSecurityUser json = build();
		
		if(Objects.nonNull(q.getId())) {json.setId(user.getId());}
		if(Objects.nonNull(q.getEmail())) {json.setEmail(user.getEmail());}
		if(Objects.nonNull(q.getFirstName())) {json.setFirstName(user.getFirstName());}
		if(Objects.nonNull(q.getLastName())) {json.setLastName(user.getLastName());}
		
		return json;
	}
}