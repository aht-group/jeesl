package org.jeesl.api.rest.rs.jx.iot;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.iot.JeeslIotMatrixDisplayRestInterface;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevice;
import org.jeesl.model.json.io.iot.matrix.JsonMatrixDevices;

@Path("/iot/matrix")
public interface JeeslIotMatrixDisplayRest extends JeeslIotMatrixDisplayRestInterface
{	
	@GET @Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	JsonMatrixDevices devices();
	
	@GET @Path("/{code}")
	@Produces(MediaType.APPLICATION_JSON)
	JsonMatrixDevice deviceJson(@PathParam("code") String code);
}