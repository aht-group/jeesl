package org.jeesl.api.rest.rs.module.news;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.aht.Aht;

public interface JeeslNewsRestExport
{
	@GET @Path("/system/news/category") @Produces(MediaType.APPLICATION_XML)
	Aht exportSystemNewsCategories();
}