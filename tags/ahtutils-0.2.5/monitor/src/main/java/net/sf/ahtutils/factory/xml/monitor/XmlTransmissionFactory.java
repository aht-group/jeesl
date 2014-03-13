package net.sf.ahtutils.factory.xml.monitor;

import net.sf.ahtutils.xml.monitoring.Transmission;
import net.sf.exlp.factory.xml.identity.XmlUserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTransmissionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTransmissionFactory.class);
	
	public static Transmission build(String account, String password)
	{
		Transmission transmission = new Transmission();		
		transmission.setUser(XmlUserFactory.build(account, password));
		
		return transmission;
	}
}