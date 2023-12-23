package org.jeesl.interfaces.controller;

import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.model.xml.io.mail.EmailAddress;

public interface JeeslMail<TEMPLATE extends JeeslIoTemplate<?,?,?,?,?,?>>
{
	TEMPLATE getTemplate();
	
	void overrideRecipients(EmailAddress email);
	public void spool();
}