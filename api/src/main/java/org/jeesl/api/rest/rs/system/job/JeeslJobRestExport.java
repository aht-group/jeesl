package org.jeesl.api.rest.rs.system.job;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

public interface JeeslJobRestExport
{
	@GET @Path("/system/job/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemJobCategories();
	
	@GET @Path("/system/job/status") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemJobStatus();
	
	@GET @Path("/system/job/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemJobType();
	
	@GET @Path("/system/job/feedback") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemJobFeedback();
}