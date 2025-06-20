package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayIoReportVersionMarker extends JeeslFlywayVersionMarker
{
//	void markIoReport();
	void sinceIoReport(int i);
}