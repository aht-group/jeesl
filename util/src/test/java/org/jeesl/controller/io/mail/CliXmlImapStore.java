package org.jeesl.controller.io.mail;

import javax.mail.MessagingException;

import org.exlp.interfaces.system.property.ConfigKey;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.io.mail.imap.XmlImapStore;
import org.jeesl.model.xml.io.mail.Mails;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliXmlImapStore
{
	final static Logger logger = LoggerFactory.getLogger(CliXmlImapStore.class);
	
	private XmlImapStore xmlImapStore;
	
	public CliXmlImapStore(XmlImapStore store)
	{
		this.xmlImapStore=store;
		
	}
	
	public void unread() throws MessagingException
	{
		xmlImapStore.useInbox();
		logger.info("Unread: "+xmlImapStore.countUnread());
		
		Mails mails = xmlImapStore.listUnread();
		JaxbUtil.info(mails);
	}
	
	public static void main(String[] args) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
		XmlImapStore xmlImapStore = new XmlImapStore(config.getString(ConfigKey.netImapHost));
		xmlImapStore.authenticate(config.getString(ConfigKey.netImapUser), config.getString(ConfigKey.netImapPwd));
		
		CliXmlImapStore cli = new CliXmlImapStore(xmlImapStore);
		cli.unread();
	}
}
