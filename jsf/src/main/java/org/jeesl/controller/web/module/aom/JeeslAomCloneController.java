package org.jeesl.controller.web.module.aom;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.module.mmg.JeeslMmgClassificationCallback;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslAomCloneController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
										REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
										ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
										VIEW extends JeeslAomView<L,D,REALM,?>>
					extends AbstractJeeslWebController<L,D,LOC>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomCloneController.class);
	
	@SuppressWarnings("unused")
	private final JeeslMmgClassificationCallback callback;

	private final AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAsset;

	public JeeslAomCloneController(final JeeslMmgClassificationCallback callback,AomFactoryBuilder<L,D,REALM,?,?,?,?,ATYPE,VIEW,?,?,?,?,?,?,?,?> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.callback=callback;
		this.fbAsset=fbAsset;
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
								REALM realm)
	{
		logger.info("NYI "+fbAsset.toString());
	}
}