package org.jeesl.controller.web.c.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoGraphicFacade;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.factory.provider.io.IoLocaleFactoryProvider;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.controller.web.io.maven.JeeslIoMavenModuleCallback;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenModule;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicComponent;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.locale.IoStatus;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeEdition;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenModuleWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenModuleWc.class);
	
	private JeeslIoGraphicFacade<IoStatus,IoGraphic,IoGraphicType,IoGraphicComponent,IoGraphicShape> fGraphic;
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final JeeslIoMavenModuleCallback<IoMavenType> callback;
	
	private final List<IoMavenStructure> structures; public List<IoMavenStructure> getStructures() {return structures;}
	private final List<IoMavenEeEdition> enterpriseEditions;  public List<IoMavenEeEdition> getEnterpriseEditions() {return enterpriseEditions;}
	private final List<IoMavenType> types; public List<IoMavenType> getTypes() {return types;}
	private final List<IoMavenJdk> compilers; public List<IoMavenJdk> getCompilers() {return compilers;}
	private final List<IoMavenModule> modules; public List<IoMavenModule> getModules() {return modules;}
	private final List<IoMavenModule> childs; public List<IoMavenModule> getChilds() {return childs;}
	
	private IoMavenModule module; public IoMavenModule getModule() {return module;} public void setModule(IoMavenModule module) {this.module = module;}
	private IoMavenModule child; public IoMavenModule getChild() {return child;} public void setChild(IoMavenModule child) {this.child = child;}
	
	public JeeslIoMavenModuleWc(JeeslIoMavenModuleCallback<IoMavenType> callback)
	{
		super(IoLang.class,IoDescription.class);
		this.callback=callback;
		
		structures = new ArrayList<>();
		types = new ArrayList<>();
		enterpriseEditions = new ArrayList<>();
		compilers = new ArrayList<>();
		modules = new ArrayList<>();
		childs = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven,
							JeeslIoGraphicFacade<IoStatus,IoGraphic,IoGraphicType,IoGraphicComponent,IoGraphicShape> fGraphic)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		this.fGraphic=fGraphic;
		
		compilers.addAll(fMaven.allOrderedPositionVisible(IoMavenJdk.class));
		enterpriseEditions.addAll(fMaven.allOrderedPositionVisible(IoMavenEeEdition.class));
		structures.addAll(fMaven.allOrderedPositionVisible(IoMavenStructure.class));
		types.addAll(callback.getTypes());

		this.reloadModules();
	}
	
	private void reloadModules()
	{
		modules.clear();
		
		EjbIoMavenQuery q = EjbIoMavenQuery.instance().addRootFetch(JeeslIoMavenModule.Attributes.enterpriseEditions).addIoMavenTypes(types).distinct(true);
		modules.addAll(fMaven.fIoMavenModules(q).stream().filter(m -> Objects.isNull(m.getParent())).collect(Collectors.toList()));
		Collections.sort(modules,new PositionComparator<IoMavenModule>());
	}
	
	private void reset(boolean rChilds, boolean rChild)
	{
		if(rChilds) {childs.clear();}
		if(rChild) {child=null;}
	}
	
	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	public void selectModule() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(module));}
		this.reset(true, true);
		this.reloadModule();
		this.reloadChilds();
	}
	
	private void reloadModule()
	{
		EjbIoMavenQuery q = EjbIoMavenQuery.instance().addRootFetch(JeeslIoMavenModule.Attributes.enterpriseEditions);
		q.id(module);
		module = fMaven.fIoMavenModules(q).get(0);
	}
	
	public void addDevelopment() throws JeeslNotFoundException
	{
		this.reset(true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenModule.class));}
		module = EjbMavenModuleFactory.build();
	}
	
	public void saveModule() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(module));}
		EjbMavenModuleFactory.converter(fMaven, module);
		module = fMaven.save(module);
		this.reloadModule();
		this.reloadModules();
		this.reloadChilds();
		
		List<IoMavenModule> updates = new ArrayList<>();
		for(IoMavenModule c : childs)
		{
			if(!module.getType().equals(c.getType()))
			{
				c.setType(module.getType());
				updates.add(c);
			}
		}
		if(!updates.isEmpty())
		{
			fMaven.save(updates);
			this.reloadChilds();
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) throws JeeslNotFoundException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		try
		{
			module.setGraphic(fGraphic.fGraphic(IoMavenModule.class,module));
		}
		catch (JeeslNotFoundException e)
		{
			IoGraphicType type = fMaven.fByCode(IoGraphicType.class,JeeslGraphicType.Code.svg);
			module.setGraphic(IoLocaleFactoryProvider.svg().efGraphic().build(type));
		}
		module.getGraphic().setData(file.getContent());
	}
	
	private void reloadChilds()
	{
		this.reset(true, false);
		childs.addAll(fMaven.allForParent(IoMavenModule.class,module));
	}
	
	public void addChild() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenModule.class));}
		child = EjbMavenModuleFactory.build();
		child.setParent(module);
		child.setType(module.getType());
	}
	
	public void selectChild() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(child));}
	}
	
	public void saveChild() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(child));}
		EjbMavenModuleFactory.converter(fMaven,child);
		child.setJdk(module.getJdk());
		child = fMaven.save(child);
		this.reloadChilds();
	}
	
	public void deleteChild() throws JeeslConstraintViolationException
	{
		List<IoMavenUsage> usages = fMaven.allForParent(IoMavenUsage.class,child);
		if(debugOnInfo) {logger.info(AbstractLogMessage.deleteEntity(child)+" with "+usages.size()+" "+IoMavenUsage.class.getSimpleName());}
		fMaven.rm(usages);
		fMaven.rm(child);
		this.reset(false, true);
		this.reloadChilds();
	}
	
	public void reorderModules() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,modules); this.reloadModules();}
	public void reorderChilds() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,childs); this.reloadChilds();}
}