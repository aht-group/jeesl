package org.jeesl.factory.txt.system.io.ssi;

import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtSsiCredentialFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtSsiCredentialFactory.class);
    
	public static String debug(JsonSsiCredential json)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("REST Credentials: ");
		sb.append(" URL:").append(json.getUrl());
		sb.append(" User:").append(json.getUser());
//		sb.append(" Pwd:").append(json.getPassword());
		return sb.toString();
	}
}