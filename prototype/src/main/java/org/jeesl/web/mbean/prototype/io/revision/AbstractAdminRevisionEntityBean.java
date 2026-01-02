package org.jeesl.web.mbean.prototype.io.revision;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeesl.api.bean.JeeslLabelBean;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoLabelFacade;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.ejb.io.label.EjbLabelEntityFactory;
import org.jeesl.interfaces.bean.sb.bean.SbSingleBean;
import org.jeesl.interfaces.bean.sb.handler.SbSingleSelection;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionCategory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.label.er.JeeslRevisionDiagram;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionEntityMapping;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScope;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionScopeType;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionView;
import org.jeesl.interfaces.model.io.label.revision.core.JeeslRevisionViewMapping;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;
import org.jeesl.interfaces.rest.system.JeeslSystemRestInterface;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbSingleHandler;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.util.comparator.ejb.PositionParentComparator;
import org.jeesl.util.db.updater.JeeslDbEntityAttributeUpdater;
import org.jeesl.util.query.ejb.JeeslInterfaceAnnotationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Deprecated // Use JeeslLabelEntityController instead
public abstract class AbstractAdminRevisionEntityBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											RC extends JeeslRevisionCategory<L,D,RC,?>,
											RV extends JeeslRevisionView<L,D,RVM>,
											RVM extends JeeslRevisionViewMapping<RV,RE,REM>,
											RS extends JeeslRevisionScope<L,D,RC,RA>,
											RST extends JeeslRevisionScopeType<L,D,RST,?>,
											RE extends JeeslRevisionEntity<L,D,RC,REM,RA,ERD>,
											REM extends JeeslRevisionEntityMapping<RS,RST,RE>,
											RA extends JeeslRevisionAttribute<L,D,RE,RER,RAT>,
											RER extends JeeslStatus<L,D,RER>,
											RAT extends JeeslStatus<L,D,RAT>,
											ERD extends JeeslRevisionDiagram<L,D,RC>>
					extends AbstractAdminRevisionBean<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD>
					implements SbSingleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminRevisionEntityBean.class);

	private JeeslLabelBean<RE> bLabel;

	protected final SbSingleHandler<RC> sbhCategory; public SbSingleHandler<RC> getSbhCategory() {return sbhCategory;}
	protected final SbSingleHandler<ERD> sbhDiagram; public SbSingleHandler<ERD> getSbhDiagram() {return sbhDiagram;}
	
	protected final EjbLabelEntityFactory<L,D,RC,RV,RVM,RE,REM,RA,RER,RAT,ERD> efEntity;
	
	protected final List<RE> entities; public List<RE> getEntities() {return entities;}
	private List<RE> links; public List<RE> getLinks() {return links;}
	private List<ERD> diagrams; public List<ERD> getDiagrams() {return diagrams;}

	private RE entity; public RE getEntity() {return entity;} public void setEntity(RE entity) {this.entity = entity;}
	private REM mapping; public REM getMapping() {return mapping;}public void setMapping(REM mapping) {this.mapping = mapping;}

	private String className; public String getClassName() {return className;}
	private Map<String, List<String>> mapEntitesCodeToAttribustes;

	private boolean supportsJeeslDownloadTranslation; public boolean isSupportsJeeslDownloadTranslation(){return supportsJeeslDownloadTranslation;}
	private boolean supportsJeeslAttributeDownload; public boolean isSupportsJeeslAttributeDownload() {return supportsJeeslAttributeDownload;}
	
	public AbstractAdminRevisionEntityBean(final IoRevisionFactoryBuilder<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD,?> fbRevision)
	{
		super(fbRevision);
		
		sbhCategory = new SbSingleHandler<>(fbRevision.getClassCategory(),this);
		sbhDiagram = new SbSingleHandler<>(fbRevision.getClassDiagram(),this);
		
		efEntity = fbRevision.ejbEntity();
		
		mapEntitesCodeToAttribustes = new HashMap<String,List<String>>();
		
		entities = new ArrayList<>();
	}

	protected void postConstructRevisionEntity(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage, JeeslIoLabelFacade<L,D,RC,RV,RVM,RS,RST,RE,REM,RA,ERD,?> fRevision, JeeslLabelBean<RE> bLabel)
	{
		if(jogger!=null) {jogger.start("postConstructRevisionEntity");}
		super.postConstructRevision(bTranslation,bMessage,fRevision);
		this.bLabel=bLabel;
		
		sbhCategory.setList(fRevision.allOrderedPositionVisible(fbRevision.getClassCategory()));
		sbhCategory.setDefault();
		
		sbhDiagram.setList(fRevision.allForParent(fbRevision.getClassDiagram(),sbhCategory.getSelection()));
		sbhDiagram.setDefault();

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
	
	@Override public void selectSbSingle(SbSingleSelection handler, EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(item.getClass().isAssignableFrom(fbRevision.getClassCategory()))
		{
			logger.info(AbstractLogMessage.selectEntity(sbhCategory.getSelection()));
			sbhDiagram.setList(fRevision.allForParent(fbRevision.getClassDiagram(),sbhCategory.getSelection()));
			sbhDiagram.setDefault();
			callbackAfterSbSelection();
		}
		if(item.getClass().isAssignableFrom(fbRevision.getClassDiagram()))
		{
			logger.info(AbstractLogMessage.selectEntity(sbhDiagram.getSelection()));
			callbackAfterSbSelection();
		}
	}
	
	private void reset(boolean rEntity, boolean rAttribute)
	{
		if(rEntity) {supportsJeeslAttributeDownload=false;}
		if(rAttribute) {attribute=null;}
	}
	
	@Override public void callbackAfterSbSelection()
	{
		reloadEntities();
	}
	
	private void reloadEntities()
	{
		entities.clear();
		
		entities.addAll(fRevision.findLabelEntities(sbhCategory.getSelection(),sbhDiagram.getSelection()));

		if(jogger!=null) {jogger.milestone(fbRevision.getClassEntity().getSimpleName(),"Entities", entities.size());}
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbRevision.getClassEntity(),entities));}
		Collections.sort(entities,cpEntity);
	}
	
	public void searchMisconfigured()
	{
		entities.clear();
		for(RE ejb : fRevision.all(fbRevision.getClassEntity()))
		{
			if(efEntity.checkMisconfiguration(ejb))
			{		
				entities.add(ejb);
			}
		}
	}

//	private void prepareEntitiesCodeMap()
//	{
//		mapEntitesCodeToAttribustes = new HashMap<String,List<String>>();
//		if(!sbhCategory.getHasSelected())
//		{
//			for(Iterator<RE> iterator = entities.iterator(); iterator.hasNext();)
//			{
//				RE re = iterator.next();
//
//				ArrayList<String> raCodes = new ArrayList<String>();
//				for(RA ra : re.getAttributes())
//				{
//					raCodes.add(ra.getCode());
//				}
//				mapEntitesCodeToAttribustes.put(re.getCode(), raCodes);
//			}
//		}
//	}

//	public boolean hasEntitiesWithShortCode(String missingEntityCode)
//	{
//		for (Iterator<String> iterator = mapEntitesCodeToAttribustes.keySet().iterator(); iterator.hasNext();)
//		{
//			String code = iterator.next();
//			if(code.endsWith("." + missingEntityCode))
//			{
//				//logger.info("code ends with" + missingEntityCode);
//				return true;
//			}
//		}
//		return false;
//	}

//	public boolean hasEntitiesWithShortCodeAndAttribute(String missingEntityCode, String missingAttributeCode)
//	{
//		for (Entry<String, List<String>> entry : mapEntitesCodeToAttribustes.entrySet())
//		{
//			if(entry.getKey().endsWith("." + missingEntityCode))
//			{
//				for (Iterator<String> iterator = entry.getValue().iterator(); iterator.hasNext();)
//				{
//					String raCode = iterator.next();
//					if(raCode.equals(missingAttributeCode))
//					{
//						//logger.info("attribute code ends with" + missingAttributeCode);
//						return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

//ToDo: Sachin : test these methods call these method from hasEntitiesWithShortCode and hasEntitiesWithShortCodeAndAttribute
//	public void updateWithAutomatedTranslation(String missingEntityCode, String missingAttributeCode, String defaultLangCode, String langCode)
//	{
//		selectMissingEntityAttribute(missingEntityCode, missingAttributeCode);
//		if(Objects.isNull(missingAttributeCode))
//		{
//			String defaultLangText = entity.getName().get(defaultLangCode).getLang();
//			String automatedTranslatedText = callRemoteWebserviceForTranslation(defaultLangText,defaultLangCode,langCode);
//			entity.getName().get(defaultLangCode).setLang(automatedTranslatedText);
//		}
//		entity=null;
//		attributes=null;
//		attribute=null;
//	}
//
//	private String callRemoteWebserviceForTranslation(String defaultLangText, String defaultLangCode, String langCode)
//	{
//		Form form = new Form();
//		form.param("q", defaultLangText)
//			.param("source", defaultLangCode)
//			.param("target", langCode);
//		Entity<Form> entity = Entity.form(form);
//
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget target = client.target("https://libretranslate.com/translate");
//		Response response = target.request(MediaType.APPLICATION_JSON).post(entity);
//		String value = response.readEntity(String.class);
//        response.close();  // You should close connections!
//
//        return value;
//	}
//
//	public void selectMissingEntity(String missingEntityCode)
//	{
//		entity=null;
//		attributes=null;
//		attribute=null;
//		for (String code : mapEntitesCodeToAttribustes.keySet())
//		{
//			if(code.endsWith("." + missingEntityCode))
//			{
//				try
//				{
//					entity = fRevision.fByCode(fbRevision.getClassEntity(), code);
//					entity = fRevision.load(fbRevision.getClassEntity(), entity);
//					attributes = entity.getAttributes();
//				}
//				catch (JeeslNotFoundException e) {logger.info(code + "not found");}
//			}
//		}
//	}
//
//	public void selectMissingEntityAttribute(String missingEntityCode, String attributeCode)
//	{
//		selectMissingEntity(missingEntityCode);
//		for (RA ra : attributes)
//		{
//			if(ra.getCode().equals(attributeCode))
//			{
//				this.attribute = ra;
//			}
//		}
//	}

	public void addEntity() throws JeeslNotFoundException
	{
		reset(true,true);
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbRevision.getClassEntity()));}
		entity = efEntity.build(sbhCategory.getSelection(),entities);
		entity.setDiagram(sbhDiagram.getSelection());
		entity.setName(efLang.buildEmpty(lp.getLocales()));
		entity.setDescription(efDescription.buildEmpty(lp.getLocales()));
		attribute=null;
		mapping=null;
	}

	private void reloadEntity()
	{
		entity = fRevision.load(fbRevision.getClassEntity(), entity);
		attributes = entity.getAttributes();
		entityMappings = entity.getMaps();
		supportsJeeslAttributeDownload=false;
		try
		{
			Class<?> c = Class.forName(entity.getCode());
			supportsJeeslDownloadTranslation = JeeslInterfaceAnnotationQuery.isAnnotationPresent(DownloadJeeslDescription.class,c);
			supportsJeeslAttributeDownload = JeeslInterfaceAnnotationQuery.isAnnotationPresent(DownloadJeeslAttributes.class,c);
			
			className = c.getSimpleName();
			logger.info(c.getName()+" supportsJeeslDownloadTranslation: "+supportsJeeslDownloadTranslation);
			logger.info(c.getName()+" supportsJeeslAttributeDownload: "+supportsJeeslAttributeDownload);
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
		entity = efLang.persistMissingLangs(fRevision,lp.getLocales(),entity);
		entity = efDescription.persistMissingLangs(fRevision,lp.getLocales(),entity);
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
			entity.setJscn(entity.getCode().substring(entity.getCode().lastIndexOf(".")+1,entity.getCode().length()));
			entity = fRevision.save(entity);
			reloadEntities();
			reloadEntity();
			bMessage.growlSaved(entity);
			bLabel.reload(entity);
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintDuplicate(null);}
	}
	
	protected abstract JeeslSystemRestInterface rest(String iFqcn);
//	private JeeslSystemRest rest(String iFqcn)
//	{
//		ResteasyClient client = new ResteasyClientBuilder().build();
//		ResteasyWebTarget restTarget = client.target(JeeslLabelEntityController.urlForInterface(iFqcn));
//		JeeslSystemRest rest = restTarget.proxy(JeeslSystemRest.class);
//		return rest;
//	}
	
	public void downloadJeeslTranslations() throws UtilsConfigurationException, JeeslLockingException, JeeslNotFoundException
	{
		try
		{
			Class<?> c = Class.forName(entity.getCode());
			Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslDescription.class,c);
			
			Entity xml = this.rest(i.getName()).exportRevisionEntity(i.getName());

			if(xml==null){logger.warn("No Result from REST !!");}
			else
			{
				efLang.update(entity,xml.getLangs());
				efDescription.update(entity,xml.getDescriptions());
				saveEntity();
			}
		}
		catch (ClassNotFoundException e){e.printStackTrace();}
	}

	public void rmEntity() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(entity));}
		fRevision.rm(entity);
		bMessage.growlDeleted(entity);
		entity=null;
		mapping=null;
		attribute=null;
		
		reloadEntities();
		updatePerformed();
	}

	public void cancelEntity()
	{
		entity = null;
		attribute=null;
		mapping=null;
	}

	public void saveAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.saveEntity(attribute));}
		if(attribute.getType()!=null){attribute.setType(fRevision.find(fbRevision.getClassAttributeType(), attribute.getType()));}
		changeRelation();
		attribute = fRevision.save(fbRevision.getClassEntity(),entity,attribute);
		reloadEntity();
		bMessage.growlSaved(attribute);
		bLabel.reload(entity);
		updatePerformed();
	}

	public void rmAttribute() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(attribute));}
		fRevision.rm(fbRevision.getClassEntity(),entity,attribute);
		bMessage.growlDeleted(attribute);
		attribute=null;
		reloadEntity();
		updatePerformed();
	}
	
	public void downloadJeeslAttributes() throws UtilsConfigurationException, JeeslConstraintViolationException
	{
		reset(false,true);
		
		try
		{
			Class<?> c = Class.forName(entity.getCode());
			Class<?> i = JeeslInterfaceAnnotationQuery.findClass(DownloadJeeslAttributes.class,c);
			
			Entity xml = this.rest(i.getName()).exportRevisionEntity(i.getName());

			JeeslDbEntityAttributeUpdater<L,D,LOC,RC,RV,RVM,RS,RST,RE,REM,RA,RER,RAT,ERD> updater = new JeeslDbEntityAttributeUpdater<>(fbRevision,fRevision);
			updater.updateAttributes2(entity,lp,xml);
			reloadEntity();
		}
		catch (ClassNotFoundException e){e.printStackTrace();}
	}

	public void addMapping() throws JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.createEntity(fbRevision.getClassEntityMapping()));}
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
			bMessage.growlSaved(mapping);
			updatePerformed();
		}
		catch (JeeslConstraintViolationException e) {bMessage.constraintDuplicate(null);}
	}

	public void rmMapping() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		if(debugOnInfo){logger.info(AbstractLogMessage.deleteEntity(mapping));}
		fRevision.rm(mapping);
		bMessage.growlDeleted(mapping);
		mapping=null;
		
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

	private void addPulledField(Field f) throws InstantiationException, IllegalAccessException, JeeslLockingException, JeeslConstraintViolationException
	{
		attribute = efAttribute.build(null);
		attribute.setCode(f.getName());
		attribute.setName(efLang.createLangMap("en",StringUtils.capitalize(f.getName())));
		attribute.setDescription(efDescription.buildEmpty(lp.getLocales()));
		attribute.setEntity(entity);
		attribute.setBean(true);
		attribute.setType(getAttributeTypeFromeCode(f.getType().getSimpleName().toString()));
		attribute = fRevision.save(fbRevision.getClassEntity(),entity,attribute);
	}

	private RAT getAttributeTypeFromeCode(String code)
	{
		for(Iterator iterator = types.iterator(); iterator.hasNext();)
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
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
		{
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