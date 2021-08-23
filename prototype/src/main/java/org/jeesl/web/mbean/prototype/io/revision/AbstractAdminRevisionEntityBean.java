package org.jeesl.web.mbean.prototype.io.revision;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.io.revision.data.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.revision.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.comparator.ejb.PositionParentComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class AbstractAdminRevisionEntityBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											RC extends JeeslRevisionCategory<L,D,RC,?>,
											RV extends JeeslRevisionView<L,D,RVM>,
											RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
											RS extends JeeslRevisionScope<L,D,RC,RA>,
											RST extends JeeslStatus<L,D,RST>,
											RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
											REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
											RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>, RER extends JeeslStatus<L,D,RER>,
											RAT extends JeeslStatus<L,D,RAT>,
											ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractAdminRevisionBean<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionEntityBean.class);

	private JeeslLabelBean<RE> bLabel;

	private List<RE> links; public List<RE> getLinks() {return links;}
	private List<ERD> diagrams; public List<ERD> getDiagrams() {return diagrams;}

	private RE entity; public RE getEntity() {return entity;} public void setEntity(RE entity) {this.entity = entity;}
	private REM mapping; public REM getMapping() {return mapping;}public void setMapping(REM mapping) {this.mapping = mapping;}

	private String className; public String getClassName() {return className;}
	private Map<String, List<String>> mapEntitesCodeToAttribustes;

	public AbstractAdminRevisionEntityBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision){
		super(fbRevision);
		mapEntitesCodeToAttribustes = new HashMap<String,List<String>>();
		}

	protected void postConstructRevisionEntity(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoRevisionFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fRevision, JeeslLabelBean<RE> bLabel)
	{
		if(jogger!=null) {jogger.start("postConstructRevisionEntity");}
		super.postConstructRevision(bTranslation,bMessage,fRevision);
		this.bLabel=bLabel;

		scopes = fRevision.all(fbRevision.getClassScope());									if(jogger!=null) {jogger.milestone(fbRevision.getClassScope().getSimpleName(), null, scopes.size());}
		types = fRevision.allOrderedPositionVisible(fbRevision.getClassAttributeType());	if(jogger!=null) {jogger.milestone(fbRevision.getClassAttributeType().getSimpleName(), null, types.size());}
		relations = fRevision.allOrderedPositionVisible(fbRevision.getClassRelation());		if(jogger!=null) {jogger.milestone(fbRevision.getClassRelation().getSimpleName(), null, relations.size());}
		scopeTypes = fRevision.allOrderedPositionVisible(fbRevision.getClassScopeType());	if(jogger!=null) {jogger.milestone(fbRevision.getClassScopeType().getSimpleName(), null, scopeTypes.size());}
		diagrams = fRevision.all(fbRevision.getClassDiagram());								if(jogger!=null) {jogger.milestone(fbRevision.getClassDiagram().getSimpleName(), null, diagrams.size());}
		links = fRevision.all(fbRevision.getClassEntity());									if(jogger!=null) {jogger.milestone(fbRevision.getClassEntity().getSimpleName(),"Links", links.size());}

		Collections.sort(diagrams, new PositionParentComparator<ERD>(fbRevision.getClassDiagram()));
		Collections.sort(links,cpEntity);
		if(jogger!=null) {jogger.milestone("Sorting",null,null);}

		reloadEntities();
	}

	@Override public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		logger.info(AbstractLogMessage.toggled(c));
		super.toggled(c);

		if(c.isAssignableFrom(fbRevision.getClassCategory()))
		{
			reloadEntities();
			logger.info(AbstractLogMessage.reloaded(fbRevision.getClassEntity(),entities));
			sbhDiagram.selectNone();
		}
		else if(c.isAssignableFrom(fbRevision.getClassDiagram()))
		{
			logger.info(AbstractLogMessage.reloaded(fbRevision.getClassDiagram(),diagrams));
			if(sbhDiagram.hasSelected())
			{
				Iterator<RE> i = entities.iterator();

				while (i.hasNext())
				{
					RE re = i.next();
			        if (re.getDiagram()==null || !sbhDiagram.getSelected().contains(re.getDiagram()))
			        {
			        	i.remove();
			        }
				}
			}
		}
		cancelEntity();
	}

	private void reloadEntities()
	{
//		if(debugOnInfo) {logger.info("fRevision==null?"+(fRevision==null)+" sbhCategory==null?"+(sbhCategory==null)+" sbhCategory.getSelected()==null?"+(sbhCategory.getSelected()==null));}
		entities = fRevision.findRevisionEntities(sbhCategory.getSelected(), true);

		if(jogger!=null) {jogger.milestone(fbRevision.getClassEntity().getSimpleName(),"Entities", entities.size());}

		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbRevision.getClassEntity(),entities));}
		Collections.sort(entities,cpEntity);

		sbhDiagram.clear();
		sbhDiagram.setList(efEntity.toDiagrams(entities));
		Collections.sort(sbhDiagram.getList(),cpDiagram);
		sbhDiagram.selectAll();
		if(jogger!=null) {jogger.milestone(fbRevision.getClassDiagram().getSimpleName(),null, sbhDiagram.getList().size());}
		prepareEntitiesCodeMap();
		if(jogger!=null) {jogger.milestone(fbRevision.getClassAttribute().getSimpleName(),"prepareEntitiesCodeMap", mapEntitesCodeToAttribustes.size());}
	}

	private void prepareEntitiesCodeMap()
	{
		mapEntitesCodeToAttribustes = new HashMap<String,List<String>>();
		if(!sbhCategory.getHasSelected())
		{
			for (Iterator<RE> iterator = entities.iterator(); iterator.hasNext();)
			{
				RE re = iterator.next();

				ArrayList<String> raCodes = new ArrayList<String>();
				for(RA ra : re.getAttributes())
				{
					raCodes.add(ra.getCode());
				}
				mapEntitesCodeToAttribustes.put(re.getCode(), raCodes);
			}
		}
	}

	public boolean hasEntitiesWithShortCode(String missingEntityCode)
	{
		for (Iterator<String> iterator = mapEntitesCodeToAttribustes.keySet().iterator(); iterator.hasNext();)
		{
			String code = iterator.next();
			if(code.endsWith("." + missingEntityCode))
			{
				//logger.info("code ends with" + missingEntityCode);
				return true;
			}
		}
		return false;
	}

	public boolean hasEntitiesWithShortCodeAndAttribute(String missingEntityCode, String missingAttributeCode)
	{
		for (Entry<String, List<String>> entry : mapEntitesCodeToAttribustes.entrySet()) {
			if(entry.getKey().endsWith("." + missingEntityCode)) {
				for (Iterator<String> iterator = entry.getValue().iterator(); iterator.hasNext();) {
					String raCode = iterator.next();
					if(raCode.equals(missingAttributeCode)) {
						//logger.info("attribute code ends with" + missingAttributeCode);
						return true;
						}
				}
			}
		}
		return false;
	}

	public void selectMissingEntity(String missingEntityCode)
	{
		entity=null;
		attributes=null;
		attribute=null;
		for (String code : mapEntitesCodeToAttribustes.keySet()) {
			if(code.endsWith("." + missingEntityCode)) {
				try {
					entity = fRevision.fByCode(fbRevision.getClassEntity(), code);
					entity = fRevision.load(fbRevision.getClassEntity(), entity);
					attributes = entity.getAttributes();
				} catch (JeeslNotFoundException e) {
					logger.info(code + "not found");
				}
			}
		}
	}

	public void selectMissingEntityAttribute(String missingEntityCode, String attributeCode) {
		selectMissingEntity(missingEntityCode);
		for (RA ra : attributes) {
			if(ra.getCode().equals(attributeCode)) {
				this.attribute = ra;
			}
		}
	}

	public void addEntity() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbRevision.getClassEntity()));}
		entity = efEntity.build(null,entities);
		entity.setName(efLang.createEmpty(langs));
		entity.setDescription(efDescription.createEmpty(langs));
		attribute=null;
		mapping=null;
	}

	private void reloadEntity()
	{
		entity = fRevision.load(fbRevision.getClassEntity(), entity);
		attributes = entity.getAttributes();
		entityMappings = entity.getMaps();
		try
		{
			Class<?> c = Class.forName(entity.getCode());
			className = c.getSimpleName();
		}
		catch (ClassNotFoundException e)
		{
			className = "CLASS NOT FOUND";

		}
	}

	public void selectEntity() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(entity));}
		entity = fRevision.find(fbRevision.getClassEntity(), entity);
		entity = efLang.persistMissingLangs(fRevision,langs,entity);
		entity = efDescription.persistMissingLangs(fRevision,langs,entity);
		reloadEntity();
		attribute=null;
		mapping=null;
	}

	public void saveEntity() throws JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(entity));}

		try
		{
			if(entity.getCategory()!=null){entity.setCategory(fRevision.find(fbRevision.getClassCategory(), entity.getCategory()));}
			if(entity.getDiagram()!=null){entity.setDiagram(fRevision.find(fbRevision.getClassDiagram(), entity.getDiagram()));}
			entity = fRevision.save(entity);
			reloadEntities();
			reloadEntity();
			bMessage.growlSuccessSaved();
			bLabel.reload(entity);
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}

	}

	public void rmEntity() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(entity));}
		fRevision.rm(entity);
		entity=null;
		mapping=null;
		attribute=null;
		bMessage.growlSuccessRemoved();
		reloadEntities();
		updatePerformed();
	}

	public void cancelEntity()
	{
		entity = null;
		attribute=null;
		mapping=null;
	}

	//*************************************************************************************

	public void saveAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(attribute));}
		if(attribute.getType()!=null){attribute.setType(fRevision.find(fbRevision.getClassAttributeType(), attribute.getType()));}
		changeRelation();
		attribute = fRevision.save(fbRevision.getClassEntity(),entity,attribute);
		reloadEntity();
		bMessage.growlSuccessSaved();
		bLabel.reload(entity);
		updatePerformed();
	}

	public void rmAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(attribute));}
		fRevision.rm(fbRevision.getClassEntity(),entity,attribute);
		attribute=null;
		bMessage.growlSuccessRemoved();
		reloadEntity();
		updatePerformed();
	}

	//*************************************************************************************

	public void addMapping() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.addEntity(fbRevision.getClassEntityMapping()));}
		RST rst = null; if(!scopeTypes.isEmpty()){rst=scopeTypes.get(0);}
		mapping = efMappingEntity.build(entity,null,rst);
		updateUi();
	}

	public void selectMapping() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectEntity(mapping));}
		mapping = fRevision.find(fbRevision.getClassEntityMapping(), mapping);
		updateUi();
	}

	public void saveMapping() throws JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(mapping));}
		mapping.setScope(fRevision.find(fbRevision.getClassScope(),mapping.getScope()));
		mapping.setType(fRevision.find(fbRevision.getClassScopeType(), mapping.getType()));

		try
		{
			mapping = fRevision.save(mapping);
			updateUi();
			reloadEntity();
			bMessage.growlSuccessSaved();
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.errorConstraintViolationDuplicateObject();}
	}

	public void rmMapping() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.rmEntity(mapping));}
		fRevision.rm(mapping);
		mapping=null;
		bMessage.growlSuccessRemoved();
		reloadEntity();
		updatePerformed();
	}

	public void cancelMapping()
	{
		mapping=null;
	}

	public void changeScopeType()
	{
		mapping.setType(fRevision.find(fbRevision.getClassScopeType(), mapping.getType()));
		logger.info(AbstractLogMessage.selectEntity(mapping, mapping.getType()));
		updateUi();
	}

	//UI
	private boolean uiWithJpqlTree; public boolean isUiWithJpqlTree() {return uiWithJpqlTree;}

	private void updateUi()
	{
		uiWithJpqlTree = false;
		if(mapping!=null)
		{
			if(mapping.getType().getCode().equals(JeeslRevisionEntityMapping.Type.jpqlTree.toString())){uiWithJpqlTree=true;}
		}
	}

	public void changeRelation()
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.selectOneMenuChange(attribute.getRelation()));}
		if(attribute.getRelation()!=null) {attribute.setRelation(fRevision.find(fbRevision.getClassRelation(), attribute.getRelation()));}
		else {attribute.setEntity(null);}
		if(attribute.getEntity()!=null){attribute.setEntity(fRevision.find(fbRevision.getClassEntity(), attribute.getEntity()));}
	}

	public void reorderEntites() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, fbRevision.getClassEntity(), entities);Collections.sort(entities, cpEntity);}
	public void reorderAttributes() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, attributes);}
	public void reorderMappings() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fRevision, entityMappings);}

	protected void updatePerformed(){}

	public void initMissingLables() {
		entity=null;
		mapping=null;
		attribute=null;
	}
	//todo
	//update bean relations when all entities are saved
	@SuppressWarnings("rawtypes")
	public void pullAttributesFromClass() {
		try
		{
			Class<?> c = Class.forName(getEntity().getCode());
			List<Class> classes = new ArrayList<>();
			classes = getClasssHierarchy(classes,c);
			Class[] interfaces = c.getInterfaces();
			for (Class i : interfaces) {
				classes.add(i);
			}
			//List<Field> fields = new ArrayList<>();
			for (Class enumClass : classes) {
				try {
					Field[] allFields = enumClass.getDeclaredFields();
					for (Field f : allFields) {if(!isFieldAvilable(f,enumClass)) {addPulledField(f);}}
				} catch (InstantiationException | IllegalAccessException | JeeslLockingException | JeeslConstraintViolationException | NullPointerException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			reloadEntity();
			bMessage.growlSuccessSaved();
			bLabel.reload(entity);
		} catch (SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
			logger.warn("Pull Attributes From Class Exception: "+e.getMessage());
		}
	}
	private List<Class> getClasssHierarchy(List<Class> classes,Class<?> c)
	{
		classes.add(c);
		try
		{
			return getClasssHierarchy(classes,c.getSuperclass());

		}
		catch (NullPointerException e) {return classes;}
	}

	private void addPulledField(Field f) throws InstantiationException, IllegalAccessException, JeeslLockingException, JeeslConstraintViolationException {
		attribute = efAttribute.build(null);
		attribute.setCode(f.getName());
		attribute.setName(efLang.createLangMap("en",StringUtils.capitalize(f.getName())));
		attribute.setDescription(efDescription.createEmpty(langs));
		attribute.setEntity(entity);
		attribute.setBean(true);
		//logger.info("size:" +types.size() + types.get(0).getCode() + types.get(0).getId());
		attribute.setType(getAttributeTypeFromeCode(f.getType().getSimpleName().toString()));
		//logger.info("entity:" + entity.getId());
		attribute = fRevision.save(fbRevision.getClassEntity(),entity,attribute);
	}

	private RAT getAttributeTypeFromeCode(String code)
	{
		for (Iterator iterator = types.iterator(); iterator.hasNext();)
		{
			RAT type = (RAT) iterator.next();
			if(type.getCode().equals(code)) {return type;}
		}
		return types.get(0);
	}

	private boolean isFieldAvilable(Field f,Class<?> c)
	{
		int mod = f.getModifiers();
		//static, final and abstract field are marked as available so that its not saved for automatic download
		if(Modifier.isFinal(mod) || Modifier.isStatic(mod) || Modifier.isAbstract(mod)) {return true;}

		//check if field have getter methods
		//non getters field are marked as available so that its not saved for automatic download
		try {c.getDeclaredMethod("get"+ StringUtils.capitalize(f.getName()));}catch (Exception e) {
		try {c.getDeclaredMethod("is"+ StringUtils.capitalize(f.getName()));} catch (Exception e2) {return false;}}

		//check if field is already saved
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			RA ra = (RA) iterator.next();
			if(ra.getCode().equals(f.getName())) {return true;}
		}
		return false;
	}

	public boolean isEmptyEntityReloaded() {
		if(entity.getCode()==null || entity.getCode().isEmpty()) {return true;}
		if(className==null || className.isEmpty() || className.equals("CLASS NOT FOUND")) {
			return true;
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	@Override protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String actionDeveloper)
	{
		uiAllowSave = jsfSecurityHandler.allow(actionDeveloper);

		if(logger.isTraceEnabled())
		{
			logger.info(uiAllowSave+" allowSave ("+actionDeveloper+")");
		}
	}
}