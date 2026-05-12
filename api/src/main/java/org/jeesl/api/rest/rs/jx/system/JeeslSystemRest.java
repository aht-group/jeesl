package org.jeesl.api.rest.rs.jx.system;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.xsd.Container;

@Path("/jeesl/export")
public interface JeeslSystemRest extends JeeslSystemRestInterface
{
//	void x();
	
	@GET @Path("/status/{code}") @Produces(MediaType.APPLICATION_XML)
	Container exportStatus(@PathParam("code") String code) throws UtilsConfigurationException;
	
	@POST @Path("/status/update/{code}") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	Container updateTranslation(@PathParam("code") String code, Container xml) throws UtilsConfigurationException;
		
	@GET @Path("/revision/entity/{code}") @Produces(MediaType.APPLICATION_XML)
	Entity downloadLabelEntity(@PathParam("code") String code) throws UtilsConfigurationException;
}