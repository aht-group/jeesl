package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywaySystemJobVersionMarker extends JeeslFlywayVersionMarker,
													FlywayIoSsiVersionMarker
{		
	void sinceSystemJob(int i);
	
	void markerSystemJob();
}