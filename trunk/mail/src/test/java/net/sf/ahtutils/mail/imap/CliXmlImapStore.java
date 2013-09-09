package net.sf.ahtutils.mail.imap;

import javax.mail.MessagingException;

import net.sf.ahtutils.test.UtilsMailTestBootstrap;
import net.sf.ahtutils.xml.mail.Mails;
import net.sf.exlp.interfaces.util.ConfigKey;
import net.sf.exlp.util.xml.JaxbUtil;

import org.apache.commons.configuration.Configuration;
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
		Configuration config = UtilsMailTestBootstrap.init();
		XmlImapStore xmlImapStore = new XmlImapStore(config.getString(ConfigKey.netImapHost));
		xmlImapStore.authenticate(config.getString(ConfigKey.netImapUser), config.getString(ConfigKey.netImapPwd));
		
		CliXmlImapStore cli = new CliXmlImapStore(xmlImapStore);
		cli.unread();
	}
}
