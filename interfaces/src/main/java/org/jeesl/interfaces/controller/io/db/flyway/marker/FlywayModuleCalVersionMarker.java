package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayModuleCalVersionMarker extends JeeslFlywayVersionMarker
{
//	void markModuleCal();
	void sinceModuleCal(int i);
}