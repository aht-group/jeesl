package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.jeesl.model.ejb.system.tenant.TenantStatus;

public class FlywaySystemTenantPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{	
	public static FlywaySystemTenantPathProvider instance() {return new FlywaySystemTenantPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/system/tenant";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.addAll(FlywayIoLocalePathProvider.instance().getMdsClasses());
		list.add(TenantStatus.class);
		list.add(TenantRealm.class);
		return list;
	}
}