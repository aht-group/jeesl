package org.jeesl.factory.xml.system.security;

import org.jeesl.model.xml.system.navigation.Menu;
import org.jeesl.model.xml.system.security.Roles;
import org.jeesl.model.xml.system.security.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSecurityFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSecurityFactory.class);
		
	public static Security build()
	{
		return new Security();
	}
	
	public static Security build(Menu menu) {Security xml = build(); xml.setMenu(menu); return xml;}
	public static Security build(Roles roles) {Security xml = build(); xml.setRoles(roles); return xml;}
}