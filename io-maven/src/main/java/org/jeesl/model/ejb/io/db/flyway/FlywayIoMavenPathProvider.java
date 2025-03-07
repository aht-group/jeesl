package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeStandard;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;

public class FlywayIoMavenPathProvider implements JeeslFlywayPathProvider,JeesDdlClassProvider
{
	public static FlywayIoMavenPathProvider instance() {return new FlywayIoMavenPathProvider();}
	private FlywayIoMavenPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/maven";}

	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(IoMavenModule.class);
		list.add(IoMavenJdk.class);
		list.add(IoMavenType.class);
		list.add(IoMavenEeEdition.class);
		list.add(IoMavenStructure.class);
		
		list.add(IoMavenGroup.class);
		list.add(IoMavenArtifact.class);
		list.add(IoMavenScope.class);
		list.add(IoMavenVersion.class);
		
		list.add(IoMavenSuitability.class);
		list.add(IoMavenOutdate.class);
		list.add(IoMavenMaintainer.class);
		
		list.add(IoMavenUsage.class);
		list.add(IoMavenDependency.class);
		
		list.add(IoMavenEeEdition.class);
		list.add(IoMavenEeStandard.class);
		list.add(IoMavenEeReferral.class);
		return list;
	}
}