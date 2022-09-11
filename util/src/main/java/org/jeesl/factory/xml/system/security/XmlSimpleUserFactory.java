package org.jeesl.factory.xml.system.security;

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
		
//		if(q.isSetId()){xml.setId(user.getId());}
		if(q.isSetFirstName()){xml.setFirstName(user.getFirstName());}
		if(q.isSetLastName()){xml.setLastName(user.getLastName());}
		if(q.isSetEmail()){xml.setEmail(user.getEmail());}
		
		return xml;
	}
}