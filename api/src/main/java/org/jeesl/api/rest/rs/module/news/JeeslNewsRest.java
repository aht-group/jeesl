package org.jeesl.api.rest.rs.module.news;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.module.news.JeeslNewsRestInterface;
import org.jeesl.model.json.module.news.JsonNewsFeed;

@Path("/rest/jeesl/module/news")
public interface JeeslNewsRest extends JeeslNewsRestInterface
{
	@GET @Path("/feed/{realm}/{feed}/{locale}") @Produces(MediaType.APPLICATION_JSON)
	JsonNewsFeed feed(@PathParam("realm") Long realm, @PathParam("feed") Long feed, @PathParam("locale") String localeCode);
	
}