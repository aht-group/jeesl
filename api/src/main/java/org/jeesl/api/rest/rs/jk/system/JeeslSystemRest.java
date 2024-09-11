package org.jeesl.api.rest.rs.jk.system;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.xsd.Container;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/rest/jeesl/export")
public interface JeeslSystemRest extends JeeslSystemRestInterface
{	
	@GET @Path("/status/{code}") @Produces(MediaType.APPLICATION_XML)
	Container exportStatus(@PathParam("code") String code) throws UtilsConfigurationException;
	
	@POST @Path("/status/update/{code}") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	Container updateTranslation(@PathParam("code") String code, Container xml) throws UtilsConfigurationException;
		
	@GET @Path("/revision/entity/{code}") @Produces(MediaType.APPLICATION_XML)
	Entity exportRevisionEntity(@PathParam("code") String code) throws UtilsConfigurationException;
}