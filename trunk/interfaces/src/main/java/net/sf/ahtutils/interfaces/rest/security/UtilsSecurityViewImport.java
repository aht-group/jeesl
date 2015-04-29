package net.sf.ahtutils.interfaces.rest.security;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.access.Access;
import net.sf.ahtutils.xml.sync.DataUpdate;

public interface UtilsSecurityViewImport
{
	@POST @Path("/admin/security/views")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	DataUpdate iuSecurityViews(Access views);
}
