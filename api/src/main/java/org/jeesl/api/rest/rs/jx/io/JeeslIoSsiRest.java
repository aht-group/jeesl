package org.jeesl.api.rest.rs.jx.io;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.io.JeeslIoSsiRestInterface;
import org.jeesl.interfaces.util.qualifier.JeeslRestSecured;
import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

@Path("/jeesl/io/ssi")
public interface JeeslIoSsiRest extends JeeslIoSsiRestInterface
{
	@JeeslRestSecured
	@GET @Path("/system/{code}/credentials")
	@Produces(MediaType.APPLICATION_JSON)
	JsonSsiSystem getUrlCredentials(@PathParam("code") String code);
	
	@GET @Path("/system/{code}/host/{host}/nat")
	@Produces(MediaType.APPLICATION_JSON)
	JsonSsiContainer getNat(@PathParam("code") String system, @PathParam("host") String host);
}