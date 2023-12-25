package org.jeesl.api.rest.rs.system.job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.module.job.Jobs;

public interface JeeslJobRest
{
	@GET @Path("/queue/grab/{type}/{max:[1-9][0-9]*}") @Produces(MediaType.APPLICATION_XML)
	Jobs grab(@PathParam("type") String type, @PathParam("max") int max);
}