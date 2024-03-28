package org.jeesl.web.mbean.prototype.io.dms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoDmsFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.builder.io.IoDmsFactoryBuilder;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsFactory;
import org.jeesl.factory.ejb.io.dms.EjbIoDmsViewFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDms;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsDocument;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsLayer;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsSection;
import org.jeesl.interfaces.model.io.dms.JeeslIoDmsView;
import org.jeesl.interfaces.model.io.domain.JeeslDomainSet;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslFileStorage;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractAdminDmsConfigBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													R extends JeeslTenantRealm<L,D,R,?>,
													DMS extends JeeslIoDms<L,D,STORAGE,AS,DS,S>,
													STORAGE extends JeeslFileStorage<L,D,?,?,?>,
													AS extends JeeslAttributeSet<L,D,R,?,?>,
													DS extends JeeslDomainSet<L,D,?>,
													S extends JeeslIoDmsSection<L,D,S>,
													F extends JeeslIoDmsDocument<L,S,FC,AC>,
													VIEW extends JeeslIoDmsView<L,D,DMS>,
													LAYER extends JeeslIoDmsLayer<VIEW,AI>,
													FC extends JeeslFileContainer<?,?>,
													AI extends JeeslAttributeItem<?,AS>,
													AC extends JeeslAttributeContainer<?,?>>
					extends AbstractDmsBean<L,D,LOC,DMS,STORAGE,AS,DS,S,F,VIEW,LAYER,FC,AI,AC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminDmsConfigBean.class);
	
	private final IoAttributeFactoryBuilder<L,D,?,?,?,?,?,AS,?,?,?> fbAttribute;
	
	private final EjbIoDmsFactory<DMS> efDms;
	private final EjbIoDmsViewFactory<DMS,VIEW> efView;
	
	private List<DMS> dms; public List<DMS> getDms() {return dms;}
	protected final List<AS> sets; public List<AS> getSets() {return sets;}
	protected final List<DS> domainSets; public List<DS> getDomainSets() {return domainSets;}
	protected final List<STORAGE> storages; public List<STORAGE> getStorages() {return storages;}
	private List<VIEW> dmsViews; public List<VIEW> getDmsViews() {return dmsViews;}

	private VIEW dmsView; public VIEW getDmsView() {return dmsView;} public void setDmsView(VIEW dmsView) {this.dmsView = dmsView;}

	public AbstractAdminDmsConfigBean(IoDmsFactoryBuilder<L,D,LOC,DMS,STORAGE,S,F,VIEW,LAYER> fbDms,
										final IoAttributeFactoryBuilder<L,D,R,?,?,?,?,AS,?,?,?> fbAttribute)
	{
		super(fbDms);
		this.fbAttribute=fbAttribute;
		efDms = fbDms.ejbDms();
		efView = fbDms.ejbView();
		
		sets = new ArrayList<AS>();
		domainSets = new ArrayList<DS>();
		storages = new ArrayList<STORAGE>();
	}
	
	protected void initDmsConfig(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,JeeslIoDmsFacade<L,D,LOC,DMS,STORAGE,AS,DS,S,F,VIEW,FC,AC> fDms)
	{
		super.initDms(bTranslation,bMessage,fDms);
		initPageConfiguration();
		reloadDms();
	}
	protected abstract void initPageConfiguration();
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		
	}
	
	public void cancelView() {reset(true);}
	private void reset(boolean rView)
	{
		if(rView) {dmsView = null;}
	}
	
	private void reloadDms()
	{
		dms = fDms.all(fbDms.getClassDms());
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbDms.getClassDms(),dms));}
	}
	
	public void addDm()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbDms.getClassDms()));}
		dm = efDms.build();
		dm.setName(efLang.createEmpty(localeCodes));
//		dm.setDescription(efDescription.createEmpty(localeCodes));
		dm.setRoot(efSection.build(null));
	}
	
	public void saveDm() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(dm));}
		dm.setSet(fDms.find(fbAttribute.getClassSet(),dm.getSet()));
		dm.setStorage(fDms.find(fbDms.getClassStorage(),dm.getStorage()));
		
		dm = fDms.save(dm);
		reloadDms();
	}
	
	public void deleteDm() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(dm));}
//		fAttribute.rm(set);
//		reloadSets();
//		reset(true,true);
	}
	
	public void selectDm()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(dm));}
		reloadDm();
//		set = efLang.persistMissingLangs(fAttribute,localeCodes,set);
//		set = efDescription.persistMissingLangs(fAttribute,localeCodes,set);
//		reloadItems();
//		reset(false,true);
	}
	
	private void reloadDm()
	{
		dm = fDms.find(fbDms.getClassDms(),dm);
		dmsViews = fDms.allForParent(fbDms.getClassView(), dm);
	}
	
	public void addView()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbDms.getClassView()));}
		dmsView = efView.build(dm, dmsViews);
		dmsView.setName(efLang.createEmpty(localeCodes));
	}
	
	public void saveView() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(dmsView));}

		dmsView = fDms.save(dmsView);
		reloadDm();
	}
	
	public void selectView()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(dmsView));}
	}
	
//	public void reorderSets() throws UtilsConstraintViolationException, UtilsLockingException {PositionListReorderer.reorder(fAttribute, fbAttribute.getClassSet(), sets);Collections.sort(sets, comparatorSet);}
//	public void reorderItems() throws UtilsConstraintViolationException, UtilsLockingException {PositionListReorderer.reorder(fAttribute, items);}
}