package org.jeesl.api.rest.rs.module.training;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

import net.sf.ahtutils.xml.sync.DataUpdate;

public interface JeeslTrainingRestImport
{	
	@POST @Path("/system/training/slot/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemTrainingSlotType(Container container);
}