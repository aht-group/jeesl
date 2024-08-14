package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayModuleAomVersionMarker extends JeeslFlywayVersionMarker
{
//	void markModuleAom();
	void sinceModuleAom(int i);
}