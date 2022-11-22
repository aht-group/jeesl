package org.jeesl.api.rest.rs.io.label;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Diagrams;
import org.jeesl.model.xml.system.revision.Entities;
import org.metachart.xml.graph.Graph;

public interface JeeslRevisionRestExport
{
	@GET @Path("/export/system/io/revision/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemRevisionCategories();
	
	@GET @Path("/export/system/io/revision/attribute/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoRevisionAttributeTypes();
	
	@GET @Path("/export/system/io/revision/scope/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoRevisionScopeTypes();
	
	@GET @Path("/export/system/io/revision/relation/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemRevisionRelationType();
	
	@GET @Path("/export/system/io/revision/entities") @Produces(MediaType.APPLICATION_XML)
	Entities exportSystemRevisionEntities();
	
	@GET @Path("/export/system/revision/diagrams") @Produces(MediaType.APPLICATION_XML)
	Diagrams exportSystemRevisionDiagrams();
	
	@GET @Path("/export/system/revision/graph/{code}") @Produces(MediaType.APPLICATION_XML)
	Graph exportSystemRevisionGraph(@PathParam("code") String code);
}