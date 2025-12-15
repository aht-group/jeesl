package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayIoCryptoVersionMarker;
import org.jeesl.model.ejb.io.crypto.IoCryptoKey;
import org.jeesl.model.ejb.io.crypto.IoCryptoKeyLifetime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlywayIoCryptoPathProvider implements FlywayIoCryptoVersionMarker,JeesDdlClassProvider,JeeslFlywayPathProvider
{
	final static Logger logger = LoggerFactory.getLogger(FlywayIoCryptoPathProvider.class);
	
	public static FlywayIoCryptoPathProvider instance() {return new FlywayIoCryptoPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/crypto";}
	@Override public void sinceIoCrypto(int i) {logger.trace("This is not required here, but helps to understand the flyway features");}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(IoCryptoKey.class);
		list.add(IoCryptoKeyLifetime.class);
		return list;
	}
}