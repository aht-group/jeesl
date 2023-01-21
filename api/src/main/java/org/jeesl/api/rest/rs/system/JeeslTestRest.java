package org.jeesl.api.rest.rs.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;

@Path("/rest/test")
public interface JeeslTestRest extends JeeslTestRestInterface
{
	@GET @Path("/date/time/public") @Produces(MediaType.TEXT_PLAIN) 
	String dateTimePublic();
	
	@JeeslRestSecured
	@GET @Path("/date/restricted") @Produces(MediaType.TEXT_PLAIN) 
	String dateTimeRestricted();
	
	
	@GET @Path("/json/update") @Produces(MediaType.APPLICATION_JSON) 
	JsonSsiUpdate jsonUpdate();
	
	@GET @Path("/json/job") @Produces(MediaType.APPLICATION_JSON) 
	JsonSystemJob jsonJob();
}