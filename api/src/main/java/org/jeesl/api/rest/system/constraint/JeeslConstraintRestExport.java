package org.jeesl.api.rest.system.constraint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.jeesl.Container;

import net.sf.ahtutils.xml.system.Constraints;

public interface JeeslConstraintRestExport
{
	@GET @Path("/system/constraints") @Produces(MediaType.APPLICATION_XML)
	Constraints exportConstraints();
	
	@GET @Path("/system/constraint/category") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemConstraintCategories();
	
	@GET @Path("/system/constraint/type") @Produces(MediaType.APPLICATION_XML)
	Container exportSystemConstraintTypes();
}