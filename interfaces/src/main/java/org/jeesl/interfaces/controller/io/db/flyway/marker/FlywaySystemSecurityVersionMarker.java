package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywaySystemSecurityVersionMarker extends JeeslFlywayVersionMarker
{
//	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/system/security";
	
//	public static String baselineTables = flywayMigrationPath+"/0-Baseline Tables.sql";
//	public static String baselineConstraints = flywayMigrationPath+"/0-Baseline Constraints.sql";
	
	void sinceSystemSecurity(int i);
//	public void markerSecurity2();
}