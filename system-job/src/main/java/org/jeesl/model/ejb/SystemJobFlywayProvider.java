package org.jeesl.model.ejb;

public interface SystemJobFlywayProvider extends JeeslFlywayMigrationVerifier,
												IoFrFlywayProvider
{	
	
	void sinceSystemJob(int i);
	
	void markerSystemJob();
}