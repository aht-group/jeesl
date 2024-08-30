package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayModuleTsVersionMarker extends JeeslFlywayVersionMarker
{
//	void markModuleTs();
	void sinceModuleTs(int i);
}