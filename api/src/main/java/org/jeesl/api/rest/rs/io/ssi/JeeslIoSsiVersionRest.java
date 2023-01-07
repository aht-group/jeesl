package org.jeesl.api.rest.rs.io.ssi;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;

@Path("/rest/jeesl/io/ssi")
public interface JeeslIoSsiVersionRest 
{
	@POST @Path("/library/version") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	JsonSsiContainer versionsLibrary(JsonSsiContainer ssi);
}