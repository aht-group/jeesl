package org.jeesl.api.rest.rs.module.feedback;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

public interface JeeslFeedbackRestExport
{
	@GET @Path("/module/feedback/style") @Produces(MediaType.APPLICATION_XML)
	Container exportFeedbackStyle();
	
	@GET @Path("/module/feedback/type") @Produces(MediaType.APPLICATION_XML)
	Container exportFeedbackType();
}