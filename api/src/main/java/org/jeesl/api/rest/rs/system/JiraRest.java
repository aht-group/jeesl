package org.jeesl.api.rest.rs.system;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.ssi.jira.Issue;

@Path("/rest/api/2")
public interface JiraRest
{		    
	@GET @Path("/issue/{key}") @Produces(MediaType.APPLICATION_JSON)
    Issue test(@PathParam("key") String key);
	
	@GET @Path("/issue/{key}") @Produces(MediaType.TEXT_PLAIN)
    String debug(@PathParam("key") String key);
}