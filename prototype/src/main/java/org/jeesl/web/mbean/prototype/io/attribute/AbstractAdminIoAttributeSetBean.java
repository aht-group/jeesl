package org.jeesl.web.mbean.prototype.io.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.util.comparator.ejb.io.attribute.AttributeSetComparator;
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

public abstract class AbstractAdminIoAttributeSetBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
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
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoAttributeSetBean.class);
		
	private final List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private final List<SET> sets; public List<SET> getSets() {return sets;}
	private List<ITEM> items; public List<ITEM> getItems() {return items;}
	
	private SET set; public SET getSet() {return set;} public void setSet(SET set) {this.set = set;}
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}
	
	private final Comparator<SET> comparatorSet;
	
	public AbstractAdminIoAttributeSetBean(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute)
	{
		super(fbAttribute);
		comparatorSet = new AttributeSetComparator<CAT,CATEGORY,SET>().factory(AttributeSetComparator.Type.position);
		criterias = new ArrayList<>();
		sets = new ArrayList<>();
	}
	
	protected void postConstructAttributeSet(R realm,
												JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
												JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
												JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		super.postConstructAttribute(realm,bTranslation,bMessage,bAttribute,fAttribute);
	}
	
//	@Deprecated
//	protected void initAttributeSet1(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
//									JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute,
//									JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
//	{
//		super.initAttribute(bTranslation,bMessage,bAttribute,fAttribute);
//		reloadSets();
//	}
	
	protected void updateRealm(RREF rref)
	{
		this.reset(true,true,true);
		this.rref=rref;
		reloadCategories();
		
		criterias.addAll(fAttribute.fAttributeCriteria(realm,rref,sbhCat.getSelected()));
		logger.info("Criterias: "+realm.toString()+" "+rref.toString()+" "+criterias.size());
		reloadSets();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbAttribute.getClassCategory().isAssignableFrom(c))
		{
			reloadSets();
		}
	}
	
	public void resetAll() {reset(true,true,true);}
	public void resetSet() {reset(false,true,true);}
	private void reset(boolean rCriterias, boolean rSet, boolean rItem)
	{
		if(rCriterias) {criterias.clear();}
		if(rSet) {set=null;}
		if(rItem){item=null;}
	}
	
	protected void reloadSets()
	{
		sets.clear();
		if(realm!=null && rref!=null) {sets.addAll(fAttribute.fAttributeSets(realm,rref,sbhCat.getSelected()));}
//		if(refId<0){}
//		else {sets.addAll(fAttribute.fAttributeSets(sbhCategory.getSelected(), refId));}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassSet(),sets));}
	}
	
	public void addSet()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassCriteria()));}
		set = efSet.build(realm,rref,sbhCat.getSelected().get(0));
		set.setName(efLang.createEmpty(localeCodes));
		set.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void saveSet() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(set));}
		efSet.converter(fAttribute, set);
		set = fAttribute.save(set);
		reloadSets();
		reloadItems();
	}
	
	public void deleteSet() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(set));}
		fAttribute.rm(set);
		reloadSets();
		reset(false,true,true);
	}
	
	public void selectSet()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(set));}
		set = efLang.persistMissingLangs(fAttribute,localeCodes,set);
		set = efDescription.persistMissingLangs(fAttribute,localeCodes,set);
		reloadItems();
		reset(false,false,true);
	}
	
	private void reloadItems()
	{
		items = fAttribute.allForParent(fbAttribute.getClassItem(), set);
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassItem(),items));}
	}
	
	public void addItem()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassItem()));}
		item = efItem.build(null,set,items);
	}
	
	public void saveItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(item));}
		item.setItemSet(fAttribute.find(fbAttribute.getClassSet(),item.getItemSet()));
		item.setCriteria(fAttribute.find(fbAttribute.getClassCriteria(),item.getCriteria()));
		item = fAttribute.save(item);
		reloadItems();
		bAttribute.updateSet(item.getItemSet());
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(item));}
		fAttribute.rm(item);
		reset(false,false,true);
		reloadItems();
	}
	
	public void selectItem()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(item));}
	}
	
	public void reorderSets() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, fbAttribute.getClassSet(), sets);Collections.sort(sets, comparatorSet);}
	public void reorderItems() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, items);}
}