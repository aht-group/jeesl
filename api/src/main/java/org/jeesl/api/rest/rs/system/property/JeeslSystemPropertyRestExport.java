package org.jeesl.api.rest.rs.system.property;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.aht.Container;

public interface JeeslSystemPropertyRestExport
{
	@GET @Path("/system/property/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemPropertyCategories();
	
	@GET @Path("/system/property/values") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemProperties();
}