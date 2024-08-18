package org.jeesl.model.ejb.io.db.flyway;

import java.util.ArrayList;
import java.util.List;

import org.jeesl.interfaces.controller.io.db.JeesDdlClassProvider;
import org.jeesl.interfaces.controller.io.db.flyway.JeeslFlywayPathProvider;
import org.jeesl.interfaces.controller.io.db.flyway.marker.FlywayModuleAomVersionMarker;
import org.jeesl.model.ejb.module.aom.asset.AomAsset;
import org.jeesl.model.ejb.module.aom.asset.AomAssetStatus;
import org.jeesl.model.ejb.module.aom.asset.AomAssetType;
import org.jeesl.model.ejb.module.aom.asset.AomView;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.module.aom.company.AomCompanyScope;
import org.jeesl.model.ejb.module.aom.event.AomEvent;
import org.jeesl.model.ejb.module.aom.event.AomEventStatus;
import org.jeesl.model.ejb.module.aom.event.AomEventType;

public class FlywayModuleAomPathProvider implements JeeslFlywayPathProvider, JeesDdlClassProvider, FlywayModuleAomVersionMarker
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
		
		list.add(AomAsset.class);
		list.add(AomAssetStatus.class);
		list.add(AomAssetType.class);
		list.add(AomView.class);
		
		list.add(AomEvent.class);
		list.add(AomEventStatus.class);
		list.add(AomEventType.class);
		
		return list;
	}

	@Override public void sinceModuleAom(int i) {}
}