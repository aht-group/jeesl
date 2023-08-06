package org.jeesl.model.ejb;

public interface IoSsiFlywayProvider extends JeeslFlywayMigrationVerifier //extends IoLocaleFlywayProvider
{	
	public static final String flywayMigrationPath = "jeesl/system/io/db/migration/io/ssi";
	void implementsOfIoSsiSince(int i);
}