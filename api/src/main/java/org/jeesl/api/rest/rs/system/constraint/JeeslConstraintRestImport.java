package org.jeesl.api.rest.rs.system.constraint;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.xml.system.constraint.Constraints;
import org.jeesl.model.xml.xsd.Container;

import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public interface JeeslConstraintRestImport
{
	@POST @Path("/system/constraints") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importConstraints(Constraints constraints);
	
	@POST @Path("/system/constraint/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemConstraintCategories(Container categories);
	
	@POST @Path("/system/constraint/type") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemConstraintTypes(Container types);
}