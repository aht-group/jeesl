package org.jeesl.controller.io.mail.msg;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class JeeslMimeMessage extends MimeMessage
{
	private String msgId;

	public JeeslMimeMessage(Session session, String msgId)
	{
		super(session);
		this.msgId=msgId;
	}

	@Override
	protected void updateMessageID() throws MessagingException
	{
		this.
		setHeader("Message-ID", msgId);
	}
}