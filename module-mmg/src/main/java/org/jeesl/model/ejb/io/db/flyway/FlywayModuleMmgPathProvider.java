package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.module.mmg.MmgClassification;
import org.jeesl.model.ejb.module.mmg.MmgGallery;
import org.jeesl.model.ejb.module.mmg.MmgItem;
import org.jeesl.model.ejb.module.mmg.MmgQuality;

public class FlywayModuleMmgPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{
	public static FlywayModuleMmgPathProvider instance() {return new FlywayModuleMmgPathProvider();}
	private FlywayModuleMmgPathProvider() {}
	
	@Override public String getRootDirectory() {return "jeesl/io/db/migration/module/mmg";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(MmgClassification.class);
		list.add(MmgGallery.class);
		list.add(MmgItem.class);
		list.add(MmgQuality.class);
		
		return list;
	}
}