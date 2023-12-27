package org.jeesl.api.rest.rs.module.feedback;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.xsd.Container;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public interface JeeslFeedbackRestImport
{
	@POST @Path("/module/feedback/style") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importFeedbackStyle(Container container);
	
	@POST @Path("/module/feedback/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importFeedbackType(Container container);
}