package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywaySystemConstraintVersionMarker extends JeeslFlywayVersionMarker
{	
	void sinceSystemConstraint(int i);
	
//	public void markerSystemConstraint();
}