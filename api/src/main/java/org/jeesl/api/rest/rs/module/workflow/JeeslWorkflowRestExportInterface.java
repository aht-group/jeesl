package org.jeesl.api.rest.rs.module.workflow;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.module.workflow.Workflow;

public interface JeeslWorkflowRestExportInterface
{
	@GET @Path("/context") @Produces(MediaType.APPLICATION_XML)
	Container exportWorkflowContext();
	
	@GET @Path("/processes") @Produces(MediaType.APPLICATION_XML)
	Workflow exportWorkflowProcesses();
}