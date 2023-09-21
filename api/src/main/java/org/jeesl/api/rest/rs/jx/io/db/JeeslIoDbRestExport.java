package org.jeesl.api.rest.rs.jx.io.db;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

public interface JeeslIoDbRestExport
{
//	@GET @Path("/system/db/activity/state") @Produces(MediaType.APPLICATION_XML)
//	Container exportSystemDbActivityState();
	
	@GET @Path("/system/db/dump/status") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoDbDumpStatus();
}