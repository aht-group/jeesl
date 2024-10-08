package org.jeesl.controller.web.c.io.maven;

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
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.controller.web.util.AbstractLogMessage;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenModuleFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenUsageComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslIoMavenUsageWc extends AbstractJeeslLocaleWebController<IoLang,IoDescription,IoLocale>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenUsageWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven;

	private final Comparator<IoMavenArtifact> cpArtifact;
	private final Comparator<IoMavenUsage> cpUsage;
	
	private final Map<IoMavenType,List<IoMavenModule>> mapModule; public Map<IoMavenType,List<IoMavenModule>> getMapModule() {return mapModule;}
	private final Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> mapVersion; public Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> getMapVersion() {return mapVersion;}
	
	private final List<IoMavenType> types; public List<IoMavenType> getTypes() {return types;}
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenModule> modules;  public List<IoMavenModule> getModules() {return modules;}

	private IoMavenModule development; 
	
	public JeeslIoMavenUsageWc()
	{
		super(IoLang.class,IoDescription.class);
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		cpUsage = EjbMavenUsageComparator.instance(EjbMavenUsageComparator.Type.version);
		
		mapModule = new HashMap<>();
		mapVersion = new HashMap<>();
		
		types = new ArrayList<>();
		artifacts = new ArrayList<>();
		modules = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenType,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		super.postConstructLocaleWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		types.addAll(fMaven.allOrderedPositionVisible(IoMavenType.class));
		
		this.reloadDevelopments();
		this.reloadUsages();
	}
	
	private void reloadDevelopments()
	{
		modules.clear();
		modules.addAll(fMaven.all(IoMavenModule.class).stream().filter(m -> Objects.isNull(m.getParent())).collect(Collectors.toList()));
		
		mapModule.clear();
		mapModule.putAll(EjbMavenModuleFactory.toMapModule(modules));
		
		modules.clear();
		for(IoMavenType t : types)
		{
			if(mapModule.containsKey(t))
			{	
				for(IoMavenModule m : mapModule.get(t))
				{
					modules.add(m);
				}
			}
		}
	}
	
	private void reloadUsages()
	{
		mapVersion.clear();
		artifacts.clear();
		
		List<IoMavenUsage> list = fMaven.all(IoMavenUsage.class);
		Collections.sort(list,cpUsage);
//		Collections.reverse(list);
		
		mapVersion.putAll(EjbMavenUsageFactory.toMapArtifactRootModuleUsages(list));
		
		artifacts.addAll(mapVersion.keySet());
		Collections.sort(artifacts,cpArtifact);
	}
	
	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
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