package org.jeesl.api.rest.system;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Entity;

@Path("/rest/jeesl/export")
public interface JeeslSystemRest <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									G extends JeeslGraphic<?,?,?>>
								extends JeeslSystemRestInterface<L,D,R,G>
{	
	@GET @Path("/status/{code}") @Produces(MediaType.APPLICATION_XML)
	<X extends JeeslStatus<L,D,X>> Container exportStatus(@PathParam("code") String code) throws UtilsConfigurationException;
	
	@POST @Path("/status/update/{code}") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	<X extends JeeslStatus<L,D,X>> Container updateTranslation(@PathParam("code") String code, Container xml) throws UtilsConfigurationException;
		
	@GET @Path("/revision/entity/{code}") @Produces(MediaType.APPLICATION_XML)
	Entity exportRevisionEntity(@PathParam("code") String code) throws UtilsConfigurationException;
}