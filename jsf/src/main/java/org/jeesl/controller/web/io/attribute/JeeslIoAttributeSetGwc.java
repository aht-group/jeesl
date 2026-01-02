package org.jeesl.controller.web.io.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.util.comparator.ejb.io.attribute.AttributeSetComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeItemFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeSetFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoAttributeSetGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
												R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
												CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
												
												CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,?,?,SET>,
												
												SET extends JeeslAttributeSet<L,D,R,CAT,ITEM>,
												ITEM extends JeeslAttributeItem<CRITERIA,SET>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoAttributeSetGwc.class);
	
	private JeeslIoAttributeFacade<R,CAT,CRITERIA,?,?,SET,ITEM,?,?> fAttribute;
	private JeeslAttributeBean<R,CAT,CRITERIA,?,?,SET,ITEM,?,?> bAttribute;
	
	private final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,?,?,SET,ITEM,?,?> fbAttribute;
	private final EjbAttributeSetFactory<L,D,R,CAT,SET,ITEM> efSet;
	private final EjbAttributeItemFactory<CRITERIA,SET,ITEM> efItem;
	
	private final SbSingleHandler<R> sbhRealm; public final SbSingleHandler<R> getSbhRealm() {return sbhRealm;}
	private final SbSingleHandler<RREF> sbhRref; public final SbSingleHandler<RREF> getSbhRref() {return sbhRref;}
	private final SbMultiHandler<CAT> sbhCat; public SbMultiHandler<CAT> getSbhCat() {return sbhCat;}
	
	private final List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private final List<SET> sets; public List<SET> getSets() {return sets;}
	private List<ITEM> items; public List<ITEM> getItems() {return items;}
	
	private SET set; public SET getSet() {return set;} public void setSet(SET set) {this.set = set;}
	private ITEM item; public ITEM getItem() {return item;} public void setItem(ITEM item) {this.item = item;}
	
	private final Comparator<SET> comparatorSet;
	
	public JeeslIoAttributeSetGwc(IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,?,?,SET,ITEM,?,?> fbAttribute)
	{
		super(fbAttribute.getClassL(),fbAttribute.getClassD());
		this.fbAttribute=fbAttribute;
		
		efSet = fbAttribute.ejbSet();
		efItem = fbAttribute.ejbItem();
		
		sbhRealm = new SbSingleHandler<>(fbAttribute.getClassRealm(),this);
		sbhRref = new SbSingleHandler<>(this);
		sbhCat = new SbMultiHandler<>(fbAttribute.getClassCat(),this);
		
		comparatorSet = new AttributeSetComparator<CAT,SET>().factory(AttributeSetComparator.Type.position);
		criterias = new ArrayList<>();
		sets = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp,
										JeeslIoAttributeFacade<R,CAT,CRITERIA,?,?,SET,ITEM,?,?> fAttribute,
										JeeslAttributeBean<R,CAT,CRITERIA,?,?,SET,ITEM,?,?> bAttribute,
										JeeslFacesMessageBean bMessage, R realm)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		
		this.fAttribute=fAttribute;
		this.bAttribute=bAttribute;
		
		sbhRealm.add(realm);
		sbhRealm.setDefault();
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.reset(true,true,true);
		
		if(!sbhRref.getList().contains(rref)) {sbhRref.getList().add(rref);}
		sbhRref.setDefault(rref);
		
		this.reloadCategories();
		
		criterias.addAll(fAttribute.fAttributeCriteria(sbhRealm.getSelection(),sbhRref.getSelection(),sbhCat.getSelected()));
		logger.info("Criterias: "+sbhRealm.getSelection().toString()+" "+rref.toString()+" "+criterias.size());
		this.reloadSets();
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(handler.equals(sbhCat))
		{
			reloadSets();
		}
	}
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{

	}
	
	protected void reloadCategories()
	{
		sbhCat.clear();
		if(ObjectUtils.allNotNull(sbhRealm.getSelection(),sbhRref.getSelection()))
		{
			List<CAT> list = fAttribute.all(fbAttribute.getClassCat(),sbhRealm.getSelection(),sbhRref.getSelection());
			sbhCat.setList(list);
		}
		sbhCat.selectAll();
		sbhCat.debug(false);
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassCat(),sbhCat.getList()));}
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
		if(sbhRealm.getSelection()!=null && sbhRref.getSelection()!=null) {sets.addAll(fAttribute.fIoAttributeSets(sbhRealm.getSelection(),sbhRref.getSelection(),sbhCat.getSelected()));}
//		if(refId<0){}
//		else {sets.addAll(fAttribute.fAttributeSets(sbhCategory.getSelected(), refId));}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassSet(),sets));}
	}
	
	public void addSet()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassCriteria()));}
		set = efSet.build(sbhRealm.getSelection(),sbhRref.getSelection(),sbhCat.getSelected().get(0));
		set.setName(efLang.buildEmpty(lp.getLocales()));
		set.setDescription(efDescription.buildEmpty(lp.getLocales()));
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
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(set));}
		fAttribute.rm(set);
		reloadSets();
		reset(false,true,true);
	}
	
	public void selectSet()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(set));}
		set = efLang.persistMissingLangs(fAttribute,lp,set);
		set = efDescription.persistMissingLangs(fAttribute,lp.getLocales(),set);
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
		item.setSet(fAttribute.find(fbAttribute.getClassSet(),item.getSet()));
		item.setCriteria(fAttribute.find(fbAttribute.getClassCriteria(),item.getCriteria()));
		item = fAttribute.save(item);
		reloadItems();
		bAttribute.updateSet(item.getSet());
	}
	
	public void deleteItem() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(item));}
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