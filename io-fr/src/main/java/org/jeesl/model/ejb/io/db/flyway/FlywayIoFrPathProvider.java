package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.fr.IoFileMeta;
import org.jeesl.model.ejb.io.fr.IoFileReplication;
import org.jeesl.model.ejb.io.fr.IoFileReplicationInfo;
import org.jeesl.model.ejb.io.fr.IoFileReplicationType;
import org.jeesl.model.ejb.io.fr.IoFileStatus;
import org.jeesl.model.ejb.io.fr.IoFileStorage;
import org.jeesl.model.ejb.io.fr.IoFileStorageEngine;
import org.jeesl.model.ejb.io.fr.IoFileStorageType;
import org.jeesl.model.ejb.io.fr.IoFileType;

public class FlywayIoFrPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{
	public static FlywayIoFrPathProvider instance() {return new FlywayIoFrPathProvider();}
	
	private FlywayIoFrPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/io/db/migration/io/fr";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(IoFileContainer.class);
		list.add(IoFileMeta.class);
		list.add(IoFileReplication.class);
		list.add(IoFileReplicationInfo.class);
		list.add(IoFileReplicationType.class);
		list.add(IoFileStatus.class);
		list.add(IoFileStorage.class);
		list.add(IoFileStorageEngine.class);
		list.add(IoFileStorageType.class);
		list.add(IoFileType.class);
		
		return list;
	}
}