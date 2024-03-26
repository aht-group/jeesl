package org.jeesl.api.rest.i.io;

import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;

public interface JeeslIoMailRestInterface
{
	Mails spool();
	Mail confirm(long id);
	Mails discard(int days);
}