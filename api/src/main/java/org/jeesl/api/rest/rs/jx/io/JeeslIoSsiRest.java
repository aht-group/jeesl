package org.jeesl.api.rest.rs.jx.io;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.io.JeeslIoSsiRestInterface;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

@Path("/rest/jeesl/io/ssi")
public interface JeeslIoSsiRest extends JeeslIoSsiRestInterface
{
	@GET @Path("/system/{code}/credentials")
	@Produces(MediaType.APPLICATION_JSON)
	JsonSsiSystem getCredentials(@PathParam("code") String code);
}