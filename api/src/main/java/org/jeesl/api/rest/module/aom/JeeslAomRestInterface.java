package org.jeesl.api.rest.module.aom;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate;

public interface JeeslAomRestInterface
{	
	@GET @Path("/fix/markup") @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate fixMarkup();
}