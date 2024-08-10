package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.module.aom.company.AomCompanyScope;

public class FlywayModuleAomPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{	
	public static FlywayModuleAomPathProvider instance() {return new FlywayModuleAomPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/module/aom";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(AomCompany.class);
		list.add(AomCompanyScope.class);
		return list;
	}
}