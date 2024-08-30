package org.jeesl.controller.web.c.module.aom;

import java.io.Serializable;

import org.jeesl.controller.web.module.aom.JeeslAomCompanyController;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.ssi.core.IoSsiSystem;
import org.jeesl.model.ejb.module.aom.asset.AomAsset;
import org.jeesl.model.ejb.module.aom.asset.AomAssetStatus;
import org.jeesl.model.ejb.module.aom.asset.AomAssetType;
import org.jeesl.model.ejb.module.aom.asset.AomView;
import org.jeesl.model.ejb.module.aom.company.AomCompany;
import org.jeesl.model.ejb.module.aom.company.AomCompanyScope;
import org.jeesl.model.ejb.module.aom.event.AomEvent;
import org.jeesl.model.ejb.module.aom.event.AomEventStatus;
import org.jeesl.model.ejb.module.aom.event.AomEventType;
import org.jeesl.model.ejb.module.aom.event.AomEventUpload;
import org.jeesl.model.ejb.system.security.user.SecurityUser;
import org.jeesl.model.ejb.system.tenant.TenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomComapnyWc extends JeeslAomCompanyController<IoLang,IoDescription,IoLocale,TenantRealm,IoSsiSystem,AomCompany,AomCompanyScope>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomComapnyWc.class);

	public JeeslAomComapnyWc(AomFactoryBuilder<IoLang,IoDescription,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventType,AomEventStatus,IoMarkup,IoMarkupType,SecurityUser,IoFileContainer,AomEventUpload> fbAsset)
	{
		super(fbAsset);
	}
}