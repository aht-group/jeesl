package org.jeesl.controller.web.module.aom;

import java.io.Serializable;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.module.JeeslAssetFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.AomFactoryBuilder;
import org.jeesl.interfaces.cache.module.aom.JeeslAomCompanyCache;
import org.jeesl.interfaces.cache.module.aom.JeeslAssetCacheBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomCompany;
import org.jeesl.interfaces.model.module.aom.company.JeeslAomScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.many.JeeslSelectManyCodeHandler;
import org.jeesl.model.ejb.system.tenant.TenantIdentifier;
import org.jeesl.web.model.module.aom.AssetCompanyLazyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslAomCompanyController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											REALM extends JeeslTenantRealm<L,D,REALM,?>, RREF extends EjbWithId,
											COMPANY extends JeeslAomCompany<REALM,SCOPE>,
											SCOPE extends JeeslAomScope<L,D,SCOPE,?>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAomCompanyController.class);
	
	protected JeeslAssetFacade<L,D,REALM,COMPANY,?,?,?,?,?,?,?,?,?,?> fAsset;
	private JeeslAomCompanyCache<REALM,COMPANY> cache;
	
	private final AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,?,?,?,?,?,?,?,?,?,?,?,?> fbAsset;
	
	private JeeslSelectManyCodeHandler<SCOPE> smh; public JeeslSelectManyCodeHandler<SCOPE> getSmh() {return smh;}
	
	private AssetCompanyLazyModel<REALM,RREF,COMPANY,SCOPE> lazyCompany; public AssetCompanyLazyModel<REALM,RREF,COMPANY,SCOPE> getLazyCompany() {return lazyCompany;}

	private RREF rref; public RREF getRref() {return rref;}
	private TenantIdentifier<REALM> identifier;
	private COMPANY company; public COMPANY getCompany() {return company;} public void setCompany(COMPANY company) {this.company = company;}

	public JeeslAomCompanyController(AomFactoryBuilder<L,D,REALM,COMPANY,SCOPE,?,?,?,?,?,?,?,?,?,?,?,?> fbAsset)
	{
		super(fbAsset.getClassL(),fbAsset.getClassD());
		this.fbAsset=fbAsset;
		smh = new JeeslSelectManyCodeHandler<>();
		lazyCompany = new AssetCompanyLazyModel<>();
	}

	public void postConstruct(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage,
			JeeslAomCompanyCache<REALM,COMPANY> cache,
			JeeslAssetFacade<L,D,REALM,COMPANY,?,?,?,?,?,?,?,?,?,?> fAsset,
			REALM realm)
	{
		super.postConstructWebController(lp,bMessage);
		this.fAsset=fAsset;
		this.cache=cache;
		lazyCompany.setCache(cache);
		identifier = TenantIdentifier.instance(realm);
		smh.updateList(fAsset.allOrderedPosition(fbAsset.getClassScope()));
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.rref=rref;
		identifier.withRref(rref);
		lazyCompany.updateIdentifier(identifier);
	}
	
	public void addManufacturer() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAsset.getClassCompany()));}
		smh.clear();
		company = fbAsset.ejbManufacturer().build(identifier);
	}
	
	public void saveManufacturer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		company.setScopes(smh.toEjb());
		company = fAsset.save(company);
		cache.update(identifier,company);
	}
	
	public void selectManufacturer() throws JeeslConstraintViolationException, JeeslLockingException
	{
		company = fAsset.find(fbAsset.getClassCompany(),company);
		smh.init(company.getScopes());
	}
}