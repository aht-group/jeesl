package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.attribute.IoAttributeSet;

public class FlywayIoAttributePathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{	
	public static FlywayIoAttributePathProvider instance() {return new FlywayIoAttributePathProvider();}
	private FlywayIoAttributePathProvider() {}

	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/attribute";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(IoAttributeSet.class);
		
		
		return list;
	}
}