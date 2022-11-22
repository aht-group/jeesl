package org.jeesl.api.rest.rs.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.module.ts.JsonTsSeries;

@Path("/rest/jeesl/health")
public interface JeeslSystemHealthRest
{
	@GET @Path("ts/{indicator}/{minutes}")
	@Produces(MediaType.APPLICATION_JSON)
	JsonTsSeries timeseries(@PathParam("indicator") String indicator, @PathParam("minutes") int minutes);
}