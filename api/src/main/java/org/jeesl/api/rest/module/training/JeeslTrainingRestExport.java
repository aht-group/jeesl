package org.jeesl.api.rest.module.training;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

public interface JeeslTrainingRestExport
{
	@GET @Path("/system/training/slot/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemTrainingSlotType();
}