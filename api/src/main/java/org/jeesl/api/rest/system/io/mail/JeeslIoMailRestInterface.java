package org.jeesl.api.rest.system.io.mail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.system.io.mail.Mail;
import org.jeesl.model.xml.system.io.mail.Mails;

public interface JeeslIoMailRestInterface
{
	@GET @Path("/queue/grab") @Produces(MediaType.APPLICATION_XML)
	Mails spool();
	
	@GET @Path("/queue/confirm/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Mail confirm(@PathParam("id") long id);
}