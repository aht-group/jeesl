package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttribute;
import org.jeesl.model.ejb.io.label.entity.IoLabelAttributeType;
import org.jeesl.model.ejb.io.label.entity.IoLabelCategory;
import org.jeesl.model.ejb.io.label.entity.IoLabelEntity;
import org.jeesl.model.ejb.io.label.er.IoLabelDiagram;
import org.jeesl.model.ejb.io.label.er.IoLabelEntityRelation;
import org.jeesl.model.ejb.io.label.revision.IoRevisionEntityMapping;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScope;
import org.jeesl.model.ejb.io.label.revision.IoRevisionScopeType;

public class FlywayIoLabelPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider
{
	public static FlywayIoLabelPathProvider instance() {return new FlywayIoLabelPathProvider();}
	
	@Override public String getRootDirectory() {return "jeesl/system/io/db/migration/io/label";}
	
	@Override public String getBaselineTables() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlTables;}
	@Override public String getBaselineConstraints() {return this.getRootDirectory()+"/"+JeeslFlywayPathProvider.sqlConstraints;}
	
	@Override public List<Class<?>> getMdsClasses()
	{
		List<Class<?>> list = new ArrayList<>();
		
		list.add(IoLabelCategory.class);
		list.add(IoLabelDiagram.class);
		
		list.add(IoLabelEntity.class);
		list.add(IoLabelEntityRelation.class);
		
		list.add(IoLabelAttribute.class);
		list.add(IoLabelAttributeType.class);
		
		list.add(IoRevisionEntityMapping.class);
		list.add(IoRevisionScope.class);
		list.add(IoRevisionScopeType.class);
	
		return list;
	}
}