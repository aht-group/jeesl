package org.jeesl.api.rest.rs.jk.io.ssi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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