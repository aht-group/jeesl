package org.jeesl.factory.xml.system.security;

import java.util.Objects;

import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithEmail;
import org.jeesl.model.xml.io.db.query.QuerySecurity;
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
	
	public User build(USER ejb)
	{
		User xml = new User();
		if(Objects.nonNull(q.getId())) {xml.setId(ejb.getId());}
		if(Objects.nonNull(q.getFirstName())) {xml.setFirstName(ejb.getFirstName());}
		if(Objects.nonNull(q.getLastName())) {xml.setLastName(ejb.getLastName());}
		if(Objects.nonNull(q.getName()))
		{
			StringBuilder sb = new StringBuilder();
			sb.append(ejb.getFirstName());
			sb.append(" ");
			sb.append(ejb.getLastName());
			xml.setName(sb.toString().trim());
		}
		
		if(Objects.nonNull(q.getEmail()) && ejb instanceof EjbWithEmail)
		{
			EjbWithEmail email = (EjbWithEmail)ejb;
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