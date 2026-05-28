package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayIoAttributeVersionMarker extends JeeslFlywayVersionMarker
{		
	void implementsOfIoAttributeSince(int i);
	
//	public void markIoAttribute();
}