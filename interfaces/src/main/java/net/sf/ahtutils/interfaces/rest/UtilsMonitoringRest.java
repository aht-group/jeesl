package net.sf.ahtutils.interfaces.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.module.monitoring.ProcessingResult;
import org.jeesl.model.xml.module.monitoring.Transmission;

@Path("/monitoring")
public interface UtilsMonitoringRest
{
	
	@GET @Path("ping")
	void ping();
	
	@POST @Path("upload")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	ProcessingResult upload(Transmission transmission);
}
