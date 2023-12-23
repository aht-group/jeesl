package org.jeesl.mail.imap;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlImapStore
{
	final static Logger logger = LoggerFactory.getLogger(XmlImapStore.class);
	
	private String hostName,hostScheme;
	
	private Store store;
	private Folder folder;
	
	public XmlImapStore(String hostName)
	{
		this(hostName,"imaps");
	}
	
	public XmlImapStore(String hostName, String hostScheme)
	{
		this.hostName=hostName;
		this.hostScheme=hostScheme;
		logger.info("Connecting "+hostScheme+"://"+hostName);		
	}
	
	public void authenticate(String user, String password) throws MessagingException
	{
		Session session = Session.getDefaultInstance(new Properties(), null);
		store = session.getStore(hostScheme);
		store.connect(hostName, user, password);
	}
	
	public void useInbox() throws MessagingException {useFolder("Inbox");}
	
	public void useFolder(String folderName) throws MessagingException
	{
		folder = store.getFolder(folderName);
		folder.open(Folder.READ_ONLY);
	}
	
	public int countUnread() throws MessagingException
	{
		return folder.getUnreadMessageCount();
	}
	
	public Mails listUnread() throws MessagingException
	{
		FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
		Message messages[] = folder.search(ft);
		
		XmlMailFactory f = new XmlMailFactory();
		
		Mails mails = new Mails();
		for(Message message:messages)
		{
			Mail mail = f.build(message); 
			mails.getMail().add(mail);
/*			
			System.out.println(message.getMessageNumber()+" "+message.getHeader("Message-ID")[0]);
			
			Enumeration<Header> enu = message.getAllHeaders();
			while(enu.hasMoreElements())
			{
				Header h = enu.nextElement();
				System.out.println("  "+h.getName()+": "+h.getValue());
			}
*/		}
		return mails;
	}
	
	public void list() throws MessagingException
	{
		Folder inbox = store.getFolder("Inbox");
		inbox.open(Folder.READ_ONLY);
		int count = inbox.getMessageCount();
		Message messages[] = inbox.getMessages(count-5,count);
		
//			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
//			Message messages[] = inbox.search(ft);
		for(Message message:messages)
		{
			System.out.println(message.getMessageNumber()+" "+message.getHeader("Message-ID")[0]);
		}
	}
}
