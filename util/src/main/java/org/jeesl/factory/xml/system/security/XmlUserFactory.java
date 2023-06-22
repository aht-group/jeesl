package org.jeesl.factory.xml.system.security;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.model.xml.jeesl.QuerySecurity;
import org.jeesl.model.xml.system.security.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUserFactory<USER extends JeeslUser<?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlUserFactory.class);
		
	private final User q;
	
	public static <USER extends JeeslUser<?>> XmlUserFactory<USER> instance(User q)
	{
		return new XmlUserFactory<>(q);
	}
	
	public XmlUserFactory(QuerySecurity query){this(query.getUser());}
	public XmlUserFactory(User q)
	{
		this.q=q;
	}
	
	public User build(USER user)
	{
		User xml = new User();
		if(q.isSetId()){xml.setId(user.getId());}
		if(q.isSetFirstName()){xml.setFirstName(user.getFirstName());}
		if(q.isSetLastName()){xml.setLastName(user.getLastName());}
		if(q.isSetName())
		{
			StringBuilder sb = new StringBuilder();
			sb.append(user.getFirstName());
			sb.append(" ");
			sb.append(user.getLastName());
			xml.setName(sb.toString().trim());
		}
		
		if(q.isSetEmail() && user instanceof EjbWithEmail)
		{
			EjbWithEmail email = (EjbWithEmail)user;
			xml.setEmail(email.getEmail());
		}
		
		return xml;
	}
	
	public static User create(String firstName, String lastName)
	{
		User xml = new User();
		xml.setFirstName(firstName);
		xml.setLastName(lastName);
		return xml;
	}
	
	public static User build(String firstName, String lastName, String email)
	{
		User xml = new User();
		xml.setFirstName(firstName);
		xml.setLastName(lastName);
		xml.setEmail(email);
		return xml;
	}
}