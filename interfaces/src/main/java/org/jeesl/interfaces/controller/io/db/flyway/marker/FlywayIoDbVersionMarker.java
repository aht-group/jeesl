package org.jeesl.interfaces.controller.io.db.flyway.marker;

import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayVersionMarker;

public interface FlywayIoDbVersionMarker extends JeeslFlywayVersionMarker,FlywayIoSsiVersionMarker
{
//	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/io/db";
	
//	public static String baselineTables = "jeesl/system/io/db/migration/io/db/0-Baseline Tables.sql";
//	public static String baselineConstraints = "jeesl/system/io/db/migration/io/db/0-Baseline Constraints.sql";
	
	void implementsOfIoDbSince(int i);
	
//	public void x();
}