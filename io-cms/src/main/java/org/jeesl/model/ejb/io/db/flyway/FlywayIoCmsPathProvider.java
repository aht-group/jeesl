package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.cms.authoring.IoCmsEditStatus;
import org.jeesl.model.ejb.io.cms.authoring.IoCmsVisibility;
import org.jeesl.model.ejb.io.cms.content.IoCmsContent;
import org.jeesl.model.ejb.io.cms.content.IoCmsElement;
import org.jeesl.model.ejb.io.cms.content.IoCmsElementType;
import org.jeesl.model.ejb.io.cms.markup.IoCmsStyle;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.cms.structure.IoCms;
import org.jeesl.model.ejb.io.cms.structure.IoCmsCategory;
import org.jeesl.model.ejb.io.cms.structure.IoCmsSection;

public class FlywayIoCmsPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{	
	public static FlywayIoCmsPathProvider instance() {return new FlywayIoCmsPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/cms";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		list.add(IoMarkup.class);
		list.add(IoMarkupType.class);
		
		list.add(IoCmsEditStatus.class);
		list.add(IoCmsVisibility.class);
		list.add(IoCmsStyle.class);
		list.add(IoCms.class);
		list.add(IoCmsCategory.class);
		list.add(IoCmsSection.class);
		list.add(IoCmsElement.class);
		list.add(IoCmsElementType.class);
		list.add(IoCmsContent.class);
		
		return list;
	}
}