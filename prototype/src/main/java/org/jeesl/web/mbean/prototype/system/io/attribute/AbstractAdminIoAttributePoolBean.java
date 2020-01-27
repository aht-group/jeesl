package org.jeesl.web.mbean.prototype.system.io.attribute;

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
import org.jeesl.interfaces.bean.sb.SbToggleBean;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeContainer;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeData;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeItem;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.jsf.util.PositionListReorderer;
import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractAdminIoAttributePoolBean <L extends UtilsLang, D extends UtilsDescription, LOC extends UtilsStatus<LOC,L,D>,
												CATEGORY extends UtilsStatus<CATEGORY,L,D>,
												CRITERIA extends JeeslAttributeCriteria<L,D,CATEGORY,TYPE>,
												TYPE extends UtilsStatus<TYPE,L,D>,
												OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
												SET extends JeeslAttributeSet<L,D,CATEGORY,ITEM>,
												ITEM extends JeeslAttributeItem<CRITERIA,SET>,
												CONTAINER extends JeeslAttributeContainer<SET,DATA>,
												DATA extends JeeslAttributeData<CRITERIA,OPTION,CONTAINER>>
					extends AbstractAdminIoAttributeBean<L,D,LOC,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminIoAttributePoolBean.class);
		
	private List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private List<OPTION> options; public List<OPTION> getOptions() {return options;}

	private CRITERIA criteria; public CRITERIA getCriteria() {return criteria;} public void setCriteria(CRITERIA criteria) {this.criteria = criteria;}
	private OPTION option; public OPTION getOption() {return option;} public void setOption(OPTION option) {this.option = option;}
	
	public AbstractAdminIoAttributePoolBean(IoAttributeFactoryBuilder<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fbAttribute){super(fbAttribute);}
	
	protected void initAttributePool(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslAttributeBean<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> bAttribute, JeeslIoAttributeFacade<L,D,CATEGORY,CRITERIA,TYPE,OPTION,SET,ITEM,CONTAINER,DATA> fAttribute)
	{
		super.initAttribute(bTranslation,bMessage,bAttribute,fAttribute);
		reloadCriterias();
	}
	
	public void toggled(Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbAttribute.getClassCategory().isAssignableFrom(c))
		{
			reloadCriterias();
		}
		sbhCategory.debug(true);
	}
	
	protected void resetAll() {reset(true,true);}
	public void resetOption() {reset(false,true);}
	public void resetCriteria() {reset(true,true);}
	private void reset(boolean rCriteria, boolean rOption)
	{
		if(rCriteria) {criteria=null;}
		if(rOption) {option=null;}
	}
	
	protected void reloadCriterias()
	{
		if(refId<0) {criterias = new ArrayList<CRITERIA>();}
		else {criterias = fAttribute.fAttributeCriteria(sbhCategory.getSelected(),refId);}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassCriteria(),criterias));}
	}
	
	public void addCriteria()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbAttribute.getClassCriteria()));}
		criteria = efCriteria.build(sbhCategory.getSelected().get(0),bAttribute.getTypes().get(0),refId);
		criteria.setName(efLang.createEmpty(localeCodes));
		criteria.setDescription(efDescription.createEmpty(localeCodes));
		reset(false,true);
	}
	
	public void saveCriteria() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(criteria));}
		criteria.setCategory(fAttribute.find(fbAttribute.getClassCategory(),criteria.getCategory()));
		criteria.setType(fAttribute.find(fbAttribute.getClassType(),criteria.getType()));
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
		reset(false,true);
	}
	
	public void deleteCriteria() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.rmEntity(criteria));}
		fAttribute.rm(criteria);
		reloadCriterias();
		reset(true,true);
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
		if(debugOnInfo) {logger.info(AbstractLogMessage.addEntity(fbAttribute.getClassOption()));}
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
		reset(false,true);
	}
	
	public void reorderCriterias() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, fbAttribute.getClassCriteria(),criterias);Collections.sort(criterias,cpCriteria);}
	public void reorderOptions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, options);}
}