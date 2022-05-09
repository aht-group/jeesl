package org.jeesl.controller.handler.module.aom;

import java.util.List;

import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.api.rest.module.aom.JeeslAomRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.factory.ejb.io.cms.EjbIoCmsMarkupFactory;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.system.io.ssi.update.JsonSsiUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AomRestHandler <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
							REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
							COMPANY extends JeeslAomCompany<REALM,SCOPE>,
							SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
							ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
							ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
							ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
							ALEVEL extends JeeslAomView<L,D,REALM,?>,
							EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
							ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
							ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
							M extends JeeslMarkup<MT>,
							MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
							USER extends JeeslSimpleUser,
							FRC extends JeeslFileContainer<?,?>,
							UP extends JeeslAomEventUpload<L,D,UP,?>>
					
					implements JeeslAomRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(AomRestHandler.class);
	
	protected final JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	
//	public static <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
//					REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
//					COMPANY extends JeeslAomCompany<REALM,SCOPE>,
//					SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
//					ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
//					ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
//					ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,ALEVEL,?>,
//					ALEVEL extends JeeslAomView<L,D,REALM,?>,
//					EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
//					ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
//					ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
//					M extends JeeslMarkup<MT>,
//					MT extends JeeslIoCmsMarkupType<L,D,MT,?>,
//					USER extends JeeslSimpleUser,
//					FRC extends JeeslFileContainer<?,?>,
//					UP extends JeeslAomEventUpload<L,D,UP,?>>
//				AomRestHandler<L,D,LOC,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP>
//				instance(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset,
//					JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset)
//	{
//		return new AomRestHandler<>(fbAsset,fAsset);
//	}
	
	public AomRestHandler(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset,
							JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset)
	{
	
		this.fbAsset=fbAsset;
		this.fAsset=fAsset;
	}
	
	

	@Override
	public JsonSsiUpdate fixMarkup()
	{
		DataUpdateTracker dut = DataUpdateTracker.instance().start();
		List<EVENT> events = fAsset.all(fbAsset.getClassEvent());
		
		MT markupType = fAsset.fByEnum(fbAsset.getClassMarkupType(), JeeslIoCmsMarkupType.Code.xhtml);
		EjbIoCmsMarkupFactory<M,MT> fMarkup = EjbIoCmsMarkupFactory.instance(fbAsset.getClassMarkup());
		
		for(EVENT e : events)
		{
			if(e.getMarkup()!=null) {dut.obsolete();}
			else
			{				
				try
				{
					M markup = fMarkup.build(markupType);
					markup.setContent(e.getRemark());
					markup = fAsset.save(markup);
					e.setMarkup(markup);
					fAsset.save(e);
					dut.success();
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e1)
				{
					
				}
			}
		}
		
		return dut.toJson();
	}
}