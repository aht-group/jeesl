package org.jeesl.controller.web.io.attribute;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeCriteriaFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeOptionFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.util.comparator.ejb.system.io.attribute.AttributeCriteriaComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslAttributePoolController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
						R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
						CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
						CATEGORY extends JeeslStatus<L,D,CATEGORY>,
						CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,CATEGORY,TYPE,OPTION>,
						TYPE extends JeeslStatus<L,D,TYPE>,
						OPTION extends JeeslAttributeOption<L,D,CRITERIA>>
					extends AbstractJeeslWebController<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslAttributePoolController.class);
	
	protected JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> fAttribute;
	protected JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> bAttribute;
	
	protected final IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> fbAttribute;
	
	protected final EjbAttributeCriteriaFactory<L,D,R,CAT,CATEGORY,CRITERIA,TYPE> efCriteria;
	protected final EjbAttributeOptionFactory<CRITERIA,OPTION> efOption;
	
	protected final SbMultiHandler<CAT> sbhCat; public SbMultiHandler<CAT> getSbhCat() {return sbhCat;}
	
	private final List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private List<OPTION> options; public List<OPTION> getOptions() {return options;}

	private CRITERIA criteria; public CRITERIA getCriteria() {return criteria;} public void setCriteria(CRITERIA criteria) {this.criteria = criteria;}
	private OPTION option; public OPTION getOption() {return option;} public void setOption(OPTION option) {this.option = option;}
	
	protected final Comparator<CRITERIA> cpCriteria;
	protected R realm;
	protected RREF rref;
	protected long refId;
	
	public JeeslAttributePoolController(IoAttributeFactoryBuilder<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> fbAttribute)
	{
		super(fbAttribute.getClassL(),fbAttribute.getClassD());
		this.fbAttribute=fbAttribute;
		
		efCriteria = fbAttribute.ejbCriteria();
		efOption = fbAttribute.ejbOption();

		cpCriteria = fbAttribute.cpCriteria(AttributeCriteriaComparator.Type.position);
		
		sbhCat = new SbMultiHandler<CAT>(fbAttribute.getClassCat(),this);
		
		criterias = new ArrayList<CRITERIA>();
	}
	
	public void postConstruct(JeeslIoAttributeFacade<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> fAttribute,
								JeeslAttributeBean<L,D,R,CAT,CATEGORY,CRITERIA,TYPE,OPTION,?,?,?,?> bAttribute,
								JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage, R realm)
	{
		super.postConstructWebController(lp,bMessage);
		this.realm=realm;
		this.fAttribute=fAttribute;
		this.bAttribute=bAttribute;
	}
	
	public void updateRealmReference(RREF rref)
	{
		this.reset(true,true,true);
		this.rref=rref;
		reloadCategories();
		reloadCriterias();
	}
	
	public void toggled(Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(fbAttribute.getClassCategory().isAssignableFrom(c))
		{
			reloadCriterias();
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
		criteria.setName(efLang.buildEmpty(lp.getLocales()));
		criteria.setDescription(efDescription.buildEmpty(lp.getLocales()));
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
		criteria = efLang.persistMissingLangs(fAttribute,lp.getLocales(),criteria);
		criteria = efDescription.persistMissingLangs(fAttribute,lp.getLocales(),criteria);
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
		option.setName(efLang.buildEmpty(lp.getLocales()));
		option.setDescription(efDescription.buildEmpty(lp.getLocales()));
	}
	
	public void selectOption()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(criteria));}
		option = efLang.persistMissingLangs(fAttribute,lp.getLocales(),option);
		option = efDescription.persistMissingLangs(fAttribute,lp.getLocales(),option);
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