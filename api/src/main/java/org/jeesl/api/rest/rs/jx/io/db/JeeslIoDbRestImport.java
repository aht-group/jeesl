package org.jeesl.api.rest.rs.jx.io.db;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.xsd.Container;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.ahtutils.xml.sync.DataUpdate;

public interface JeeslIoDbRestImport
{
	@POST @Path("/system/db/activity/state") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemDbActivityState(Aht states);
	
	@POST @Path("/system/db/dump/status") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoDbDumpStatus(Container container);
	
}