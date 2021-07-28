package org.jeesl.api.rest.system.io.revision;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Entities;
import org.metachart.xml.graph.Graph;

import net.sf.ahtutils.xml.sync.DataUpdate;

public interface JeeslRevisionRestImport
{
	@POST @Path("/import/system/revision/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemRevisionCategories(Container categories);
	
	@POST @Path("/import/system/io/revision/attribute/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoRevisionAttributeTypes(Container container);
	
	@POST @Path("/import/system/io/revision/scope/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoRevisionScopeTypes(Container container);
	
	@POST @Path("/import/system/revision/entities") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemRevisionEntities(Entities entities);
	
	@POST @Path("/import/system/revision/diagram") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemRevisionDiagram(Graph graph);
}