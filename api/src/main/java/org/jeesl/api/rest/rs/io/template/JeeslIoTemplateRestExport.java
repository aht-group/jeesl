package org.jeesl.api.rest.rs.io.template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.io.template.Templates;
import org.jeesl.model.xml.xsd.Container;

public interface JeeslIoTemplateRestExport
{
	@GET @Path("/system/io/template/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoTemplateCategories();
	
	@GET @Path("/system/io/template/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoTemplateTypes();
	
	@GET @Path("/system/io/template/scope") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemIoTemplateScopes();
	
	@GET @Path("/system/io/templates") @Produces(MediaType.APPLICATION_XML)
	Templates exportSystemIoTemplates();
}