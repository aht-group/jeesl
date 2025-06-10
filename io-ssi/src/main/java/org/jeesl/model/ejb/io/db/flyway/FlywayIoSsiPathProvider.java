package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayIoSsiVersionMarker;
import org.jeesl.model.ejb.io.ssi.core.IoSsiCredential;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.jeesl.model.ejb.io.ssi.data.IoSsiContext;
import org.jeesl.model.ejb.io.ssi.data.IoSsiError;

public class FlywayIoSsiPathProvider implements JeeslFlywayPathProvider,JeesDdlClassProvider,FlywayIoSsiVersionMarker
{
	public static FlywayIoSsiPathProvider instance() {return new FlywayIoSsiPathProvider();}
	private FlywayIoSsiPathProvider() {}
	
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/ssi";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(IoSsiSystem.class);
		list.add(IoSsiHost.class);
		list.add(IoSsiCredential.class);
		
		list.add(IoSsiContext.class);
		list.add(IoSsiError.class);
		return list;
	}

	@Override public void sinceIoSsi(int i) {}
}