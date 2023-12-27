package org.jeesl.api.rest.rs.jx.io.label;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.xsd.Container;
import org.metachart.xml.graph.Graph;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

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