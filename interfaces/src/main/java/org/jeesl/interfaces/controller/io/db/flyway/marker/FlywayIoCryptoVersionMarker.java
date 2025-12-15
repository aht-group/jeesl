package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayIoCryptoVersionMarker extends JeeslFlywayVersionMarker
{
//	void markIoCrypto();
	void sinceIoCrypto(int i);
}