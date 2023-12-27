package org.jeesl.api.rest.rs.io.template;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.xsd.Container;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public interface JeeslIoTemplateRestImport
{
	@POST @Path("/system/io/template/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoTemplateCategories(Container categories);
	
	@POST @Path("/system/io/template/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoTemplateTypes(Container types);
	
	@POST @Path("/system/io/template/scopes") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemIoTemplateScopes(Container scopes);
}