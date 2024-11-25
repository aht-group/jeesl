package org.jeesl.api.rest.rs.jx.system;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.util.JsonTime;
import org.jeesl.model.xml.system.test.Test;

@Path("/test")
public interface JeeslTestRest extends JeeslTestRestInterface
{
	@GET @Path("/date/time/public") @Produces(MediaType.TEXT_PLAIN) 
	String dateTimePublic();
	
	@JeeslRestSecured
	@GET @Path("/date/restricted") @Produces(MediaType.TEXT_PLAIN) 
	String dateTimeRestricted();
	
	@GET @Path("/timeout/{seconds}") @Produces(MediaType.TEXT_PLAIN) 
	String timeout(@PathParam("seconds") int seconds);
	
	@GET @Path("/json/update") @Produces(MediaType.APPLICATION_JSON) 
	JsonSsiUpdate jsonUpdate();
	
	@GET @Path("/json/job") @Produces(MediaType.APPLICATION_JSON) 
	JsonSystemJob jsonJob();
	
	@GET @Path("/json/time/download") @Produces(MediaType.APPLICATION_JSON) 
	JsonTime jsonTimeDownload();
	
	@POST @Path("/json/time/upload") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON) 
	JsonTime jsonTimeUpload(JsonTime content);
	
	@GET @Path("/jaxb/test") @Produces(MediaType.APPLICATION_XML) 
	Test jaxb();
}