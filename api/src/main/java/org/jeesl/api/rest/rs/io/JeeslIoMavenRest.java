package org.jeesl.api.rest.rs.io;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jeesl.api.rest.i.io.JeeslIoMavenRestInterface;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

@Path("/rest/jeesl/io/maven")
public interface JeeslIoMavenRest extends JeeslIoMavenRestInterface
{
	@POST @Path("/upload/dependency/graph")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph);
	
	@POST @Path("/upload/fonts")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	JsonSsiUpdate uploadFonts(JsonMavenGraph fonts);
	
}