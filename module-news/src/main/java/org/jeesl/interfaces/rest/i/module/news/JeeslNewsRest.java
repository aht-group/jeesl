package org.jeesl.interfaces.rest.i.module.news;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.module.news.JsonNewsFeed;

@Path("/rest/jeesl/module/news")
public interface JeeslNewsRest extends JeeslNewsRestInterface
{
	@GET @Path("/feed") @Produces(MediaType.APPLICATION_JSON)
	JsonNewsFeed feed();
	
}