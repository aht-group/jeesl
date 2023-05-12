package org.jeesl.factory.builder.module;

import java.util.Comparator;

import org.jeesl.controller.util.comparator.ejb.module.aom.EjbAssetComparator;
import org.jeesl.controller.util.comparator.ejb.module.aom.EjbEventComparator;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.asset.EjbAssetCompanyFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetEventFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetLevelFactory;
import org.jeesl.factory.ejb.module.asset.EjbAssetTypeFactory;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkupType;
import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAsset;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomView;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetStatus;
import org.jeesl.interfaces.model.module.aom.asset.JeeslAomAssetType;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEvent;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventStatus;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventUpload;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AomFactoryBuilder<L extends JeeslLang,D extends JeeslDescription,
								REALM extends JeeslTenantRealm<L,D,REALM,?>,
								COMPANY extends JeeslAomCompany<REALM,SCOPE>,
								SCOPE extends JeeslAomScope<L,D,SCOPE,?>,
								ASSET extends JeeslAomAsset<REALM,ASSET,COMPANY,ASTATUS,ATYPE>,
								ASTATUS extends JeeslAomAssetStatus<L,D,ASTATUS,?>,
								ATYPE extends JeeslAomAssetType<L,D,REALM,ATYPE,VIEW,?>,
								VIEW extends JeeslAomView<L,D,REALM,?>,
								EVENT extends JeeslAomEvent<COMPANY,ASSET,ETYPE,ESTATUS,M,USER,FRC>,
								ETYPE extends JeeslAomEventType<L,D,ETYPE,?>,
								ESTATUS extends JeeslAomEventStatus<L,D,ESTATUS,?>,
								M extends JeeslIoMarkup<MT>,
								MT extends JeeslIoMarkupType<L,D,MT,?>,
								USER extends JeeslSimpleUser,
								FRC extends JeeslFileContainer<?,?>,
								UP extends JeeslAomEventUpload<L,D,UP,?>>
		extends AbstractFactoryBuilder<L,D>
{
	final static Logger logger = LoggerFactory.getLogger(AomFactoryBuilder.class);
	
	private final Class<REALM> cRealm; public Class<REALM> getClassRealm() {return cRealm;}
	private final Class<SCOPE> cScope; public Class<SCOPE> getClassScope() {return cScope;}
	private final Class<ASSET> cAsset; public Class<ASSET> getClassAsset() {return cAsset;}
	private final Class<COMPANY> cCompany; public Class<COMPANY> getClassCompany() {return cCompany;}
	private final Class<ASTATUS> cStatus; public Class<ASTATUS> getClassStatus() {return cStatus;}
	private final Class<ATYPE> cAssetType; public Class<ATYPE> getClassAssetType() {return cAssetType;}
	private final Class<VIEW> cView; public Class<VIEW> getClassAssetLevel() {return cView;}
	private final Class<EVENT> cEvent; public Class<EVENT> getClassEvent() {return cEvent;}
	private final Class<ETYPE> cEventType; public Class<ETYPE> getClassEventType() {return cEventType;}
	private final Class<ESTATUS> cEventStatus; public Class<ESTATUS> getClassEventStatus() {return cEventStatus;}
	private final Class<M> cMarkup; public Class<M> getClassMarkup() {return cMarkup;}
	private final Class<MT> cMarkupType; public Class<MT> getClassMarkupType() {return cMarkupType;}
	private final Class<UP> cUpload; public Class<UP> getClassUpload() {return cUpload;}
	
	public AomFactoryBuilder(final Class<L> cL,final Class<D> cD,
								final Class<REALM> cRealm,
								final Class<ASSET> cAsset,
								final Class<COMPANY> cCompany,
								final Class<SCOPE> cScope,
								final Class<ASTATUS> cStatus,
								final Class<ATYPE> cAssetType,
								final Class<VIEW> cView,
								final Class<EVENT> cEvent,
								final Class<ETYPE> cEventType,
								final Class<ESTATUS> cEventStatus,
								final Class<M> cMarkup,
								final Class<MT> cMarkupType,
								final Class<UP> cUpload)
	{       
		super(cL,cD);
		this.cRealm=cRealm;
		this.cCompany=cCompany;
		this.cScope=cScope;
		this.cAsset=cAsset;
		this.cStatus=cStatus;
		this.cAssetType=cAssetType;
		this.cView=cView;
		this.cEvent=cEvent;
		this.cEventType=cEventType;
		this.cEventStatus=cEventStatus;
		this.cMarkup=cMarkup;
		this.cMarkupType=cMarkupType;
		this.cUpload=cUpload;
	}
	
	public EjbAssetCompanyFactory<REALM,COMPANY,SCOPE> ejbManufacturer() {return new EjbAssetCompanyFactory<>(cCompany);}
	public EjbAssetTypeFactory<REALM,ATYPE,VIEW> ejbType() {return new EjbAssetTypeFactory<>(cAssetType);}
	public EjbAssetLevelFactory<REALM,VIEW> ejbLevel() {return new EjbAssetLevelFactory<>(cView);}
	public EjbAssetFactory<REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE> ejbAsset() {return new EjbAssetFactory<>(this);}
	public EjbAssetEventFactory<COMPANY,ASSET,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC> ejbEvent() {return new EjbAssetEventFactory<>(this);}
	
	public Comparator<ASSET> cpAsset(EjbAssetComparator.Type type){return new EjbAssetComparator<ASSET>().factory(type);}
	public Comparator<EVENT> cpEvent(EjbEventComparator.Type type){return new EjbEventComparator<EVENT>().factory(type);}
}