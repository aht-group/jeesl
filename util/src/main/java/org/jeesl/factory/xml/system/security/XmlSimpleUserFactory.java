package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.xml.system.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSimpleUserFactory<USER extends JeeslSimpleUser>
{
	final static Logger logger = LoggerFactory.getLogger(XmlSimpleUserFactory.class);
		
	private User q;
	
	public XmlSimpleUserFactory(User q)
	{
		this.q=q;
	}
	
	public User build(USER user)
	{
		User xml = new User();
		
//		if(Objects.nonNull(q.getId())) {xml.setId(user.getId());}
		if(Objects.nonNull(q.getFirstName())) {xml.setFirstName(user.getFirstName());}
		if(Objects.nonNull(q.getLastName())) {xml.setLastName(user.getLastName());}
		if(Objects.nonNull(q.getEmail())) {xml.setEmail(user.getEmail());}
		
		return xml;
	}
}