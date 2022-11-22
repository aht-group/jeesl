package org.jeesl.api.rest.rs.io.mail;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

public interface JeeslIoMailRestExport
{
	@GET @Path("/system/io/mail/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoMailCategories();
	
	@GET @Path("/system/io/mail/status") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoMailStatus();
	
	@GET @Path("/system/io/mail/retention") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoMailRetention();
}