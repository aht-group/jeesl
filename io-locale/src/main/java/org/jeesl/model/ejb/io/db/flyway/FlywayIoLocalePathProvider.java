package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicComponent;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.locale.IoStatus;

public class FlywayIoLocalePathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{
	public static FlywayIoLocalePathProvider instance() {return new FlywayIoLocalePathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/locale";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(IoLang.class);
		list.add(IoDescription.class);
		list.add(IoLocale.class);
		list.add(IoStatus.class);
		
		list.add(IoGraphic.class);
		list.add(IoGraphicShape.class);
		list.add(IoGraphicType.class);
		list.add(IoGraphicComponent.class);
		
		return list;
	}
}