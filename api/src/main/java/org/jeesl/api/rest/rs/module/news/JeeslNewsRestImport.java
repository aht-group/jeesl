package org.jeesl.api.rest.rs.module.news;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.ahtutils.xml.aht.Aht;
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;

public interface JeeslNewsRestImport
{
	@POST @Path("/system/news/category") @Produces(MediaType.APPLICATION_XML) @Consumes(MediaType.APPLICATION_XML)
	DataUpdate importSystemNewsCategories(Aht categories);
}