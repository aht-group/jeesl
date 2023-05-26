package org.jeesl.api.rest.i.io.maven;

import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

public interface JeeslIoMavenRestInterface
{
	JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph);
	JsonSsiUpdate uploadFonts(JsonMavenGraph fonts);
}