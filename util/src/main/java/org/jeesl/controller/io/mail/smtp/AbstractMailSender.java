package org.jeesl.controller.io.mail.smtp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.jdom2.Namespace;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(AbstractMailSender.class);
	
	private String smtpHost,smtpUser,smtpPassword;
	private int smtpPort;
	
	private boolean tlsPwd,plainPwd;
	private boolean smtpDebug; public void setSmtpDebug(boolean smtpDebug) {this.smtpDebug = smtpDebug;}

	protected List<EmailAddress> alwaysBcc; public void addBcc(EmailAddress bcc){alwaysBcc.add(bcc);}
	protected EmailAddress overrideOnlyTo; public void setOverrideOnlyTo(EmailAddress overrideOnlyTo) {this.overrideOnlyTo = overrideOnlyTo;}
	
	protected Session session;
	
	protected Namespace nsMail;
	
	public AbstractMailSender(String smtpHost, int smtpPort)
	{
		this.smtpHost=smtpHost;
		this.smtpPort=smtpPort;
		logger.info("Using "+smtpHost+":"+smtpPort);
		alwaysBcc = new ArrayList<EmailAddress>();
		overrideOnlyTo = null;
		
		tlsPwd = false;
		plainPwd = false;
		smtpDebug = false;
		
		smtpUser=null;
		smtpPassword=null;
		
		nsMail = Namespace.getNamespace("http://www.jeesl.org/io/mail");
	}
	
	public void tlsPasswordAuthentication(String smtpUser, String smtpPassword)
	{
		this.smtpUser=smtpUser;
		this.smtpPassword=smtpPassword;
		tlsPwd = true;
	}
	
	public void plainPasswordAuthentication(String smtpUser, String smtpPassword)
	{
		this.smtpUser=smtpUser;
		this.smtpPassword=smtpPassword;
		plainPwd = true;
	}
	
	public void debugSettings(boolean debug)
	{
		if(debug)
		{
			logger.info("Host: {}:{}",smtpHost,smtpPort);
			logger.info("User: {}",smtpUser);
			logger.info("Pwd: {}",smtpPassword);
		}
	}
	
	protected synchronized void buildSession()
	{
		if(session==null)
		{
			logger.trace("Building "+Session.class.getSimpleName());
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", smtpPort);
			props.put("mail.smtp.auth", "false");

			if(tlsPwd)
			{
				props.put("mail.transport.protocol","smtp");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl.trust","*");
				props.put("mail.smtp.tls", "true");
				props.put("mail.smtp.user", smtpUser);
				props.put("mail.password", smtpPassword);
				
				Authenticator auth = new Authenticator()
				{
					@Override public PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(smtpUser,smtpPassword);
					}
				};
				session = Session.getInstance(props, auth);
			}
			else if(plainPwd)
			{
				props.put("mail.transport.protocol","smtp");
				props.put("mail.smtp.auth", "true");
//				props.put("mail.smtp.starttls.enable", "true");
//				props.put("mail.smtp.tls", "true");
				props.put("mail.smtp.user", smtpUser);
				props.put("mail.password", smtpPassword);
				
				Authenticator auth = new Authenticator()
				{
					@Override public PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication(smtpUser,smtpPassword);
					}
				};
				session = Session.getInstance(props, auth);
			}
			else
			{
				logger.trace("Creating Seesion with NO authentication");
				session = Session.getInstance(props, null);
			}		
			session.setDebug(smtpDebug);
		}
	}
	
	protected Transport transport; 
	
	protected synchronized void connect() throws MessagingException
	{
		if(transport==null)
		{
			buildSession();
			logger.trace("Building "+Transport.class.getSimpleName());
			transport = session.getTransport("smtp");
		}
		logger.trace("Checking connected");
		if(!transport.isConnected())
		{
			logger.trace("Connecting Transport");
			transport.connect();
		}
		logger.trace("Checked connected");
	}
	
	public void disconnect() throws MessagingException
	{
		if(transport!=null && transport.isConnected())
		{
			transport.close();
		}
	}
}