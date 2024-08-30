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
import org.jeesl.model.ejb.system.security.SecurityCategory;
import org.jeesl.model.ejb.system.security.access.SecurityRole;
import org.jeesl.model.ejb.system.security.access.SecurityUsecase;
import org.jeesl.model.ejb.system.security.context.SecurityContext;
import org.jeesl.model.ejb.system.security.context.SecurityMenu;
import org.jeesl.model.ejb.system.security.doc.SecurityHelp;
import org.jeesl.model.ejb.system.security.page.SecurityAction;
import org.jeesl.model.ejb.system.security.page.SecurityArea;
import org.jeesl.model.ejb.system.security.page.SecurityTemplate;
import org.jeesl.model.ejb.system.security.page.SecurityView;
import org.jeesl.model.ejb.system.security.user.SecurityUser;

public class FlywaySystemSecurityPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{	
	public static FlywaySystemSecurityPathProvider instance() {return new FlywaySystemSecurityPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/system/security";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(IoLang.class);
		list.add(SecurityRole.class);
		list.add(SecurityUsecase.class);
		list.add(SecurityContext.class);
		list.add(SecurityMenu.class);
		list.add(SecurityAction.class);
		list.add(SecurityArea.class);
		list.add(SecurityTemplate.class);
		list.add(SecurityView.class);
		list.add(SecurityCategory.class);
		list.add(SecurityUser.class);
		list.add(SecurityHelp.class);
		
		return list;
	}
}