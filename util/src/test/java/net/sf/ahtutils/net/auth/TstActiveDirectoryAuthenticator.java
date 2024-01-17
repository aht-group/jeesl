package net.sf.ahtutils.net.auth;

import org.jeesl.JeeslBootstrap;
import org.jeesl.util.db.ActiveDirectoryAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TstActiveDirectoryAuthenticator
{
	final static Logger logger = LoggerFactory.getLogger(TstActiveDirectoryAuthenticator.class);
		
	private ActiveDirectoryAuthenticator adsAuth;
	
	public TstActiveDirectoryAuthenticator()
	{
		String domain = ".local";
		String ldapHost = "ldap://192.168.x.x:389";

		adsAuth = new ActiveDirectoryAuthenticator(domain,ldapHost);
		
	}
	
	public void direct()
	{
		logger.debug(""+adsAuth.authenticate("y", ""));
	}
	
	public static void main (String[] args) throws Exception
	{
		JeeslBootstrap.init();
		TstActiveDirectoryAuthenticator test = new TstActiveDirectoryAuthenticator();
		test.direct();
	}
}