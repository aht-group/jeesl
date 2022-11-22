package org.jeesl.api.rest.rs.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;

@Path("/rest/test")
public interface JeeslTestRest
{
	@GET @Path("time")
	@Produces(MediaType.TEXT_PLAIN)
	String getTime();
	
	@GET @Path("secure")  @JeeslRestSecured
	@Produces(MediaType.TEXT_PLAIN)
	String getSecure();
}