package org.jeesl.api.rest.rs.io.maven;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

@Path("/rest/jeesl/io/maven")
public interface JeeslIoMavenRest
{
	@POST @Path("/upload/dependency/graph")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph);
}