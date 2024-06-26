package org.jeesl.controller.web.c.module.aom;

import java.io.Serializable;

import org.jeesl.controller.web.module.aom.JeeslAomAssetGwc;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.model.ejb.io.cms.markup.IoMarkup;
import org.jeesl.model.ejb.io.cms.markup.IoMarkupType;
import org.jeesl.model.ejb.io.fr.IoFileContainer;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
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

public class JeeslAomAssetWc extends JeeslAomAssetGwc<IoLang,IoDescription,IoLocale,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventType,AomEventStatus,IoMarkup,IoMarkupType,SecurityUser,IoFileContainer,AomEventUpload>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomAssetWc.class);

	public JeeslAomAssetWc(AomFactoryBuilder<IoLang,IoDescription,TenantRealm,AomCompany,AomCompanyScope,AomAsset,AomAssetStatus,AomAssetType,AomView,AomEvent,AomEventType,AomEventStatus,IoMarkup,IoMarkupType,SecurityUser,IoFileContainer,AomEventUpload> fbAsset)
	{
		super(fbAsset);
	}
}