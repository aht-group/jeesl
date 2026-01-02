package org.jeesl.controller.web.io.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslAttributeBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoAttributeFacade;
import org.jeesl.controller.util.comparator.ejb.io.attribute.AttributeCriteriaComparator;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.factory.builder.io.IoAttributeFactoryBuilder;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeCriteriaFactory;
import org.jeesl.factory.ejb.io.attribute.EjbAttributeOptionFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCategory;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeCriteria;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeOption;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeType;
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

public class JeeslIoAttributePoolGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
						R extends JeeslTenantRealm<L,D,R,?>, RREF extends EjbWithId,
						CAT extends JeeslAttributeCategory<L,D,R,CAT,?>,
						CRITERIA extends JeeslAttributeCriteria<L,D,R,CAT,TYPE,OPTION,SET>,
						TYPE extends JeeslAttributeType<L,D,TYPE,?>,
						OPTION extends JeeslAttributeOption<L,D,CRITERIA>,
						SET extends JeeslAttributeSet<L,D,R,CAT,?>>
					extends AbstractJeeslLocaleWebController<L,D,LOC>
					implements SbSingleBean,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoAttributePoolGwc.class);
	
	protected JeeslIoAttributeFacade<R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> fAttribute;
	protected JeeslAttributeBean<R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> bAttribute;
	
	protected final IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> fbAttribute;
	
	protected final EjbAttributeCriteriaFactory<L,D,R,CAT,CRITERIA,TYPE,SET> efCriteria;
	protected final EjbAttributeOptionFactory<CRITERIA,OPTION> efOption;
	
	private final SbSingleHandler<R> sbhRealm; public final SbSingleHandler<R> getSbhRealm() {return sbhRealm;}
	private final SbSingleHandler<RREF> sbhRref; public final SbSingleHandler<RREF> getSbhRref() {return sbhRref;}
	private final SbMultiHandler<CAT> sbhCat; public SbMultiHandler<CAT> getSbhCat() {return sbhCat;}
	
	private final List<CRITERIA> criterias; public List<CRITERIA> getCriterias() {return criterias;}
	private final List<SET> sets; public List<SET> getSets() {return sets;}
	private List<OPTION> options; public List<OPTION> getOptions() {return options;}

	private CRITERIA criteria; public CRITERIA getCriteria() {return criteria;} public void setCriteria(CRITERIA criteria) {this.criteria = criteria;}
	private OPTION option; public OPTION getOption() {return option;} public void setOption(OPTION option) {this.option = option;}
	
	protected final Comparator<CRITERIA> cpCriteria;
	protected long refId;
	
	public JeeslIoAttributePoolGwc(IoAttributeFactoryBuilder<L,D,R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> fbAttribute)
	{
		super(fbAttribute.getClassL(),fbAttribute.getClassD());
		this.fbAttribute=fbAttribute;
		
		efCriteria = fbAttribute.ejbCriteria();
		efOption = fbAttribute.ejbOption();

		cpCriteria = fbAttribute.cpCriteria(AttributeCriteriaComparator.Type.position);
		
		sbhRealm = new SbSingleHandler<>(fbAttribute.getClassRealm(),this);
		sbhRref = new SbSingleHandler<>(this);
		sbhCat = new SbMultiHandler<>(fbAttribute.getClassCat(),this);
		
		criterias = new ArrayList<>();
		sets = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<LOC> lp,
								JeeslIoAttributeFacade<R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> fAttribute,
								JeeslAttributeBean<R,CAT,CRITERIA,TYPE,OPTION,SET,?,?,?> bAttribute,
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
		this.reloadCriterias();
		
		sets.clear();
		sets.addAll(fAttribute.fIoAttributeSets(sbhRealm.getSelection(),sbhRref.getSelection(),sbhCat.getSelected()));
	}
	
	@Override public void toggled(SbToggleSelection handler, Class<?> c)
	{
		logger.info(AbstractLogMessage.toggled(c));
		if(handler.equals(sbhCat))
		{
			this.reloadCriterias();
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
	
	private void reset(boolean rCategories, boolean rCriteria, boolean rOption)
	{
		if(rCategories) {sbhCat.clear();}
		if(rCriteria) {criteria=null;}
		if(rOption) {option=null;}
	}
	
	protected void reloadCriterias()
	{
		criterias.clear();
		if(ObjectUtils.allNotNull(sbhRealm.getSelection(),sbhRref.getSelection()))
		{
			criterias.addAll(fAttribute.fAttributeCriteria(sbhRealm.getSelection(),sbhRref.getSelection(),sbhCat.getSelected()));
		}
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbAttribute.getClassCriteria(),criterias));}
	}
	
	public void addCriteria()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(fbAttribute.getClassCriteria()));}
		CAT c = null; if(!sbhCat.getSelected().isEmpty()) {c = sbhCat.getSelected().get(0);}
		TYPE t = null; if(bAttribute!=null && bAttribute.getTypes()!=null && !bAttribute.getTypes().isEmpty()) {t = bAttribute.getTypes().get(0);}
		criteria = efCriteria.build(sbhRealm.getSelection(),sbhRref.getSelection(),c,t);
		criteria.setName(efLang.buildEmpty(lp.getLocales()));
		criteria.setDescription(efDescription.buildEmpty(lp.getLocales()));
		reset(false,false,true);
	}
	
	public void changeCriteriaType()
	{
		efCriteria.converter(fAttribute,criteria);
	}
	public void saveCriteria() throws JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(criteria));}
		logger.info("1: "+criteria.toString());
		efCriteria.converter(fAttribute,criteria);
		logger.info("2: "+criteria.toString());
		criteria = fAttribute.save(criteria);
		logger.info("3: "+criteria.toString());
//		bAttribute.updateCriteria(criteria);
		this.reloadCriterias();
		this.reloadOptions();
	}
	
	public void selectCriteria()
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(criteria));}
		criteria = efLang.persistMissingLangs(fAttribute,lp,criteria);
		criteria = efDescription.persistMissingLangs(fAttribute,lp.getLocales(),criteria);
		reloadOptions();
		reset(false,false,true);
	}
	
	public void deleteCriteria() throws JeeslConstraintViolationException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(criteria));}
		fAttribute.rm(criteria);
		reloadCriterias();
		reset(false,true,true);
	}
	
	private void reloadOptions()
	{
		options = fAttribute.allForParent(fbAttribute.getClassOption(),criteria);
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
		option = efLang.persistMissingLangs(fAttribute,lp,option);
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
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(criteria));}
		fAttribute.rm(option);
		reloadOptions();
		bAttribute.updateCriteria(option.getCriteria());
		reset(false,false,true);
	}
	
	public void reorderCriterias() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, fbAttribute.getClassCriteria(),criterias);Collections.sort(criterias,cpCriteria);}
	public void reorderOptions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fAttribute, options);}
}