package org.jeesl.web.mbean.prototype.io.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAdminIoAttributePoolBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
												CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
												CATEGORY extends JeeslStatus<L,D,CATEGORY>,
												CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
												TYPE extends JeeslStatus<L,D,TYPE>,
												OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
												SET extends JeeslAttributeSet<L,D,R,CAT,CATEGORY,ITEM>,
												ITEM extends JeeslAttributeItem<CRITERIA,SET>,
												CONTAINER extends JeeslAttributeContainer<SET,DATA>,
												DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
					extends AbstractAdminIoAttributeBean<L,D,LOC,R,RREF,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoAttributePoolBean.class);
		
	private final List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private List<OPTION> options; public List<OPTION> getOptions() {return options;}

	private CRITERIA criteria; public CRITERIA getCriteria() {return criteria;} public void setCriteria(CRITERIA criteria) {this.criteria = criteria;}
	private OPTION option; public OPTION getOption() {return option;} public void setOption(OPTION option) {this.option = option;}
	
	public AbstractAdminIoAttributePoolBean(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		super(fbAttribute);
		criterias = new ArrayList<CRITERIA>();
	}
	
	protected void postConstructAttributePool(R realm,
								JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
								JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
								JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		super.postConstructAttribute(realm,bTranslation,bMessage,bAttribute,fAttribute);
	}
	
//	protected void initAttributePool3(JeeslTranslationBean<L,D,LOC> bTranslation,
//									JeeslFacesMessageBean bMessage,
//									JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
//									JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
//	{
//		super.initAttribute(bTranslation,bMessage,bAttribute,fAttribute);
//		reloadCriterias();
//	}
	
	protected void updateRealm(RREF rref)
	{
		this.reset(true,true,true);
		this.rref=rref;
		reloadCategories();
		reloadCriterias();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(handler.equals(sbhCat))
		{
			this.reloadCriterias();
		}
	}
	
	protected void reloadCategories()
	{
		logger.warn("Relaoding v");
		sbhCat.clear();
		if(realm!=null && rref!=null)
		{
			List<CAT> list = fAttribute.all(fbAttribute.getClassCat(),realm,rref);
			sbhCat.setList(list);
		}
		sbhCat.debug(true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassCat(),sbhCat.getList()));}
	}
	
//	private void resetOption() {reset(false,false,true);}
//	private void resetCriteria() {reset(false,true,true);}
	private void reset(boolean rCategories, boolean rCriteria, boolean rOption)
	{
		if(rCategories) {sbhCat.clear();}
		if(rCriteria) {criteria=null;}
		if(rOption) {option=null;}
	}
	
	protected void reloadCriterias()
	{
		criterias.clear();
		if(realm!=null && rref!=null) {criterias.addAll(fAttribute.fAttributeCriteria(realm,rref,sbhCat.getSelected()));}
//		else if(refId<0) {}
//		else {criterias.addAll(fAttribute.fAttributeCriteria(sbhCategory.getSelected(),refId));}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassCriteria(),criterias));}
	}
	
	public void addCriteria()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassCriteria()));}
		CAT c = null; if(!sbhCat.getSelected().isEmpty()) {c = sbhCat.getSelected().get(0);}
		TYPE t = null; if(bAttribute!=null && bAttribute.getTypes()!=null && !bAttribute.getTypes().isEmpty()) {t = bAttribute.getTypes().get(0);}
		criteria = efCriteria.build(realm,rref,c,t);
		criteria.setName(efLang.createEmpty(localeCodes));
		criteria.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,false,true);
	}
	
	public void saveCriteria() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(criteria));}
		efCriteria.converter(fAttribute,criteria);
		criteria = fAttribute.save(criteria);
		bAttribute.updateCriteria(criteria);
		reloadCriterias();
		reloadOptions();
	}
	
	public void selectCriteria()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(criteria));}
		criteria = efLang.persistMissingLangs(fAttribute,localeCodes,criteria);
		criteria = efDescription.persistMissingLangs(fAttribute,localeCodes,criteria);
		reloadOptions();
		reset(false,false,true);
	}
	
	public void deleteCriteria() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(criteria));}
		fAttribute.rm(criteria);
		reloadCriterias();
		reset(false,true,true);
	}
	
	private void reloadOptions()
	{
		options = fAttribute.allForParent(fbAttribute.getClassOption(),criteria);
	}
	
	public void changeCriteriaType()
	{
		criteria.setType(fAttribute.find(fbAttribute.getClassType(),criteria.getType()));
	}
	
	public void addOption()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassOption()));}
		option = efOption.build(criteria,options);
		option.setName(efLang.createEmpty(localeCodes));
		option.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectOption()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(criteria));}
		option = efLang.persistMissingLangs(fAttribute,localeCodes,option);
		option = efDescription.persistMissingLangs(fAttribute,localeCodes,option);
	}
	
	public void saveOption() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(option));}
		option = fAttribute.save(option);
		bAttribute.updateCriteria(option.getCriteria());
		reloadOptions();
	}
	
	public void deleteOption() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(criteria));}
		fAttribute.rm(option);
		reloadOptions();
		bAttribute.updateCriteria(option.getCriteria());
		reset(false,false,true);
	}
	
	public void reorderCriterias() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, fbAttribute.getClassCriteria(),criterias);Collections.sort(criterias,cpCriteria);}
	public void reorderOptions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, options);}
}