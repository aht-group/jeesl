package org.jeesl.api.rest.rs.jx.io.mail;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.xsd.Container;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public interface JeeslIoMailRestImport
{
	@POST @Path("/system/io/mail/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoMailCategories(Container container);
	
	@POST @Path("/system/io/mail/status") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoMailStatus(Container container);
	
	@POST @Path("/system/io/mail/retention") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoMailRetention(Container container);
}