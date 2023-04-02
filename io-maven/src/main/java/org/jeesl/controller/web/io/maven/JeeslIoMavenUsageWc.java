package org.jeesl.controller.web.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.classification.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.classification.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.classification.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenUsageWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenUsageWc.class);
	
	private JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven;

	private final Comparator<IoMavenArtifact> cpArtifact;
	
	private final Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> mapVersion; public Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> getMapVersion() {return mapVersion;}
	
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenModule> modules;  public List<IoMavenModule> getModules() {return modules;}

	private IoMavenModule development; 
	
	public JeeslIoMavenUsageWc()
	{
		super(IoLang.class,IoDescription.class);
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		
		mapVersion = new HashMap<>();
		
		artifacts = new ArrayList<>();
		modules = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		this.reloadDevelopments();
		this.reloadUsages();
	}
	
	private void reloadDevelopments()
	{
		modules.clear();
		modules.addAll(fMaven.all(IoMavenModule.class).stream().filter(m -> Objects.isNull(m.getParent())).collect(Collectors.toList()));
	}
	
	private void reloadUsages()
	{
		mapVersion.clear();
		artifacts.clear();
		
		List<IoMavenUsage> list = fMaven.all(IoMavenUsage.class);
		mapVersion.putAll(EjbMavenUsageFactory.toMapArtifactRootModuleUsages(list));
		 
		artifacts.addAll(mapVersion.keySet());
		Collections.sort(artifacts,cpArtifact);
	}
	
	@Override
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	public void selectDevelopment() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(development));}
	}
	
	public void addDevelopment() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.createEntity(IoMavenModule.class));}
		development = EjbMavenModuleFactory.build();
	}
	
	public void saveDevelopment() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(development));}
		development = fMaven.save(development);
		
		this.reloadDevelopments();
	}
}