package org.jeesl.api.rest.rs.jx.io.mail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.io.JeeslIoMailRestInterface;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;

@Path("/rest/jeesl/io/mail")
public interface JeeslIoMailRest extends JeeslIoMailRestInterface
{
	@GET @Path("/queue/grab") @Produces(MediaType.APPLICATION_XML)
	Mails spool();
	
	@GET @Path("/queue/confirm/{id:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Mail confirm(@PathParam("id") long id);
	
	@GET @Path("/queue/discard/{days:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Mails discard(@PathParam("days") int days);
}