package org.jeesl.api.rest.i.io;

import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;

public interface JeeslIoMavenRestInterface
{
//	void test();
	JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph);
	JsonSsiUpdate uploadFonts(JsonMavenGraph fonts);
}