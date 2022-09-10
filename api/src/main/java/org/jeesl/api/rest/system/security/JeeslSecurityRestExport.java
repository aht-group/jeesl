package org.jeesl.api.rest.system.security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.security.Security;

public interface JeeslSecurityRestExport
{
	@GET @Path("/security/actions")
	@Produces(MediaType.APPLICATION_XML)
	Security exportSecurityActions();
	
	@GET @Path("/security/views")
	@Produces(MediaType.APPLICATION_XML)
	Security exportSecurityViews();
	
	@GET @Path("/security/roles")
	@Produces(MediaType.APPLICATION_XML)
	Security exportSecurityRoles();
	
	@GET @Path("/security/usecases")
	@Produces(MediaType.APPLICATION_XML)
	Security exportSecurityUsecases();
	
	@GET @Path("/security/doc/views")
	@Produces(MediaType.APPLICATION_XML)
	Security documentationSecurityViews();
	
	@GET @Path("/security/doc/pages")
	@Produces(MediaType.APPLICATION_XML)
	Security documentationSecurityPageActions();
	
	@GET @Path("/security/doc/usecases")
	@Produces(MediaType.APPLICATION_XML)
	Security documentationSecurityUsecases();
}
