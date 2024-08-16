package org.jeesl.model.ejb;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayIoLocaleVersionMarker;

public interface IoLogFlywayProvider extends JeeslFlywayVersionMarker,
										FlywayIoLocaleVersionMarker
{	
	
}