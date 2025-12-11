package org.jeesl.factory.txt.io.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMailEmailFactory 
{
	final static Logger logger = LoggerFactory.getLogger(TxtMailEmailFactory.class);
	
	public static String parseEmail(String input)
	{
		int indexAt = input.indexOf("@");
		
		if(indexAt>=0)
		{
			String user = input.substring(0,indexAt);
			String domain = input.substring(indexAt+1);
			
			if(user.contains("<") && user.length()>2)
			{
				int idx = user.indexOf("<");
				user = user.substring(idx+1,user.length());
			}
			if(domain.contains(">")) {domain = domain.substring(0,domain.indexOf(">"));}
			
			return user+"@"+domain;
		}
		else
		{
			return input;
		}
	}
	
	public static boolean containsUmlautOrSpecial(String email)
	{
        return email.chars().anyMatch(ch -> ch > 127);
    }
}