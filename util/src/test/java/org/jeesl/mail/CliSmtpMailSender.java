package org.jeesl.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.configuration.Configuration;
import org.jeesl.JeeslUtilTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.interfaces.util.ConfigKey;

public class CliSmtpMailSender
{
	final static Logger logger = LoggerFactory.getLogger(CliSmtpMailSender.class);
	
	private String smtpHost,smtpUser,smtpPassword;
	
	@SuppressWarnings("unused")
	private String smtpHelo;
	
	private String smtpFrom,smtpTo;
	private int smtpPort;
	
	private Session session;
	
	public CliSmtpMailSender(Configuration config)
	{
		this.smtpHost=config.getString(ConfigKey.netSmtpHost);
		this.smtpPort=config.getInt(ConfigKey.netSmtpPort);
//		this.smtpHelo=config.getString(ConfigKey.netSmtpHelo);
		
		smtpFrom = config.getString("net.smtp.test.from");
		smtpTo = config.getString("net.smtp.test.to");
	}
	
	public void authenticate(String smtpUser,String smtpPassword)
	{
		this.smtpUser=smtpUser;
		this.smtpPassword=smtpPassword;
	}
	
	public void plainSession()
	{
		Properties props = System.getProperties();
        
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.transport.protocol","smtp");
//		props.put("mail.smtp.localhost",smtpHelo);
         
		session = Session.getDefaultInstance(props);  
		session.setDebug(true);
	}
	
	public void plainAuthSession()
	{
		Properties props = System.getProperties();
        
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.transport.protocol","smtp");
		
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.password", smtpPassword);
         
        Authenticator auth = new Authenticator()
        {
            @Override public PasswordAuthentication getPasswordAuthentication()
            {
           	 return new PasswordAuthentication(smtpUser,smtpPassword);
            }
        };
        
        session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);
	}
	
	public void tlsSession()
	{
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.tls", "true");
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.password", smtpPassword);
        
        props.put("mail.debug","true");

        Authenticator auth = new Authenticator()
        {
            @Override public PasswordAuthentication getPasswordAuthentication()
            {
           	 return new PasswordAuthentication(smtpUser,smtpPassword);
            }
        };
            
       session = Session.getDefaultInstance(props, auth);
	}
	
	public void plain() throws MessagingException, UnsupportedEncodingException
	{
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(smtpFrom));
		msg.setRecipient(RecipientType.TO, new InternetAddress(smtpTo));
		
		msg.setSubject("It works!");
        msg.setText("body");
        msg.saveChanges();
		
		Transport.send(msg);
	}
	
	public static void main(String[] args) throws Exception
	{
		Configuration config = JeeslUtilTestBootstrap.init();

		CliSmtpMailSender cli = new CliSmtpMailSender(config);
//		cli.authenticate(config.getString(ConfigKey.netSmtpUser), config.getString(ConfigKey.netSmtpPwd));
		cli.plainSession();
		for(int i=0;i<1;i++)
		{
			cli.plain();
		}
	}
}