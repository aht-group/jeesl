package org.jeesl.api.rest.rs.jk.system;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.util.JsonTime;
import org.jeesl.model.xml.test.Test;

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
	
	@GET @Path("/json/time/download") @Produces(MediaType.APPLICATION_JSON) 
	JsonTime jsonTimeDownload();
	
	@POST @Path("/json/time/upload") @Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON) 
	JsonTime jsonTimeUpload(JsonTime content);
	
	@GET @Path("/jaxb/test") @Produces(MediaType.APPLICATION_XML) 
	Test jaxb();
}