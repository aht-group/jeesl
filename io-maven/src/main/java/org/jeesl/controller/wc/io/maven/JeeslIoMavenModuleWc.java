package org.jeesl.controller.wc.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenModuleWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenModuleWc.class);
	
	private JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven;

	private final List<IoMavenStructure> structures; public List<IoMavenStructure> getStructures() {return structures;}
	private final List<IoMavenType> types; public List<IoMavenType> getTypes() {return types;}
	private final List<IoMavenModule> modules; public List<IoMavenModule> getModules() {return modules;}
	private final List<IoMavenModule> childs; public List<IoMavenModule> getChilds() {return childs;}
	
	
	private IoMavenModule module; public IoMavenModule getModule() {return module;} public void setModule(IoMavenModule module) {this.module = module;}
	private IoMavenModule child; public IoMavenModule getChild() {return child;} public void setChild(IoMavenModule child) {this.child = child;}
	
	public JeeslIoMavenModuleWc()
	{
		super(IoLang.class,IoDescription.class);
		
		structures = new ArrayList<>();
		types = new ArrayList<>();
		modules = new ArrayList<>();
		childs = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		structures.addAll(fMaven.allOrderedPositionVisible(IoMavenStructure.class));
		types.addAll(fMaven.allOrderedPositionVisible(IoMavenType.class));

		this.reloadModules();
	}
	
	private void reloadModules()
	{
		modules.clear();
		modules.addAll(fMaven.all(IoMavenModule.class).stream().filter(m -> Objects.isNull(m.getParent())).collect(Collectors.toList()));
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
	
	public void selectDevelopment() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(module));}
		this.reset(true, true);
		this.reloadChilds();
	}
	
	public void addDevelopment() throws JeeslNotFoundException
	{
		this.reset(true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenModule.class));}
		module = EjbMavenModuleFactory.build();
	}
	
	public void saveDevelopment() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(module));}
		EjbMavenModuleFactory.converter(fMaven, module);
		module = fMaven.save(module);
		this.reloadModules();
	}
	
	public void handleFileUpload(FileUploadEvent event) throws JeeslNotFoundException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		if(module.getGraphic()==null)
		{
			IoGraphicType type = fMaven.fByCode(IoGraphicType.class,JeeslGraphicType.Code.svg);
			module.setGraphic(IoLocaleFactoryBuilder.svg().efGraphic().build(type));
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
	}
	
	public void selectChild() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(child));}
	}
	
	public void saveChild() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(child));}
		EjbMavenModuleFactory.converter(fMaven, child);
		child = fMaven.save(child);
		this.reloadChilds();
	}
	
	public void reorderModules() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,modules); this.reloadModules();}
	public void reorderChilds() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,childs); this.reloadChilds();}
}