package org.jeesl.api.rest.rs.io.ssi;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.io.ssi.core.Docker;

public interface JeeslIoDockerRestInterface
{	
	@POST @Path("/update") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	Docker update(Docker docker);
}