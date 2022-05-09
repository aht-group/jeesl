package org.jeesl.web.mbean.prototype.module.aom;

import java.io.Serializable;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.module.aom.JeeslAssetCacheBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.model.io.cms.JeeslIoCmsMarkupType;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.many.JeeslSelectManyCodeHandler;
import org.jeesl.model.module.aom.AssetCompanyLazyModel;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAssetCompanyBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAssetCompanyBean.class);
	
	protected JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset;
	private JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset;
	
	private JeeslSelectManyCodeHandler<SCOPE> smh; public JeeslSelectManyCodeHandler<SCOPE> getSmh() {return smh;}
	
	private AssetCompanyLazyModel<REALM,RREF,COMPANY,SCOPE> lazyCompany; public AssetCompanyLazyModel<REALM,RREF,COMPANY,SCOPE> getLazyCompany() {return lazyCompany;}

    protected REALM realm; public REALM getRealm() {return realm;}
	private RREF rref; public RREF getRref() {return rref;}
	private COMPANY company; public COMPANY getCompany() {return company;} public void setCompany(COMPANY company) {this.company = company;}

	public AbstractAssetCompanyBean(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		smh = new JeeslSelectManyCodeHandler<>();
		lazyCompany = new AssetCompanyLazyModel<>();
	}

	protected void postConstructAssetCompany(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
													JeeslAssetCacheBean<L,D,REALM,RREF,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,ETYPE,UP> bCache,
													JeeslAssetFacade<L,D,REALM,COMPANY,SCOPE,ASSET,ASTATUS,ATYPE,ALEVEL,EVENT,ETYPE,ESTATUS,M,MT,USER,FRC,UP> fAsset,
													REALM realm)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fAsset=fAsset;
		this.bCache=bCache;
		smh.updateList(fAsset.allOrderedPosition(fbAsset.getClassScope()));
		this.realm=realm;
	}
	
	protected void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		lazyCompany.setScope(bCache,rref);
	}
	
	public void addManufacturer() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbAsset.getClassCompany()));}
		smh.clear();
		company = fbAsset.ejbManufacturer().build(realm,rref);
	}
	
	public void saveManufacturer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		company.setScopes(smh.toEjb());
		company = fAsset.save(company);
		bCache.update(realm,rref,company);
	}
	
	public void selectManufacturer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		company = fAsset.find(fbAsset.getClassCompany(),company);
		smh.init(company.getScopes());
	}
}