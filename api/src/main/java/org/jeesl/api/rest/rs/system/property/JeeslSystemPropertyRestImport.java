package org.jeesl.api.rest.rs.system.property;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.aht.Container;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import net.sf.ahtutils.xml.utils.Utils;

public interface JeeslSystemPropertyRestImport
{
	@POST @Path("/system/property/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemPropertyCategories(Container categories);
	
	@POST @Path("/system/property/values") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemProperties(Utils values);
}