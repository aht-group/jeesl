package org.jeesl.controller.web.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.model.pojo.map.generic.Nested2Map;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenUsageWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
		implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenUsageWc.class);
	
	private JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven;

	private final Comparator<IoMavenArtifact> cpArtifact;
	
	private final Nested2Map<IoMavenArtifact,IoMavenModule,IoMavenUsage> n2m; public Nested2Map<IoMavenArtifact, IoMavenModule, IoMavenUsage> getN2m() {return n2m;}
	
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenModule> developments;  public List<IoMavenModule> getDevelopments() {return developments;}

	private IoMavenModule development; 
	
	public JeeslIoMavenUsageWc()
	{
		super(IoLang.class,IoDescription.class);
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		
		n2m = new Nested2Map<>();
		artifacts = new ArrayList<>();
		developments = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;
		
		this.reloadDevelopments();
		this.reloadUsages();
	}
	
	private void reloadDevelopments()
	{
		developments.clear();
		developments.addAll(fMaven.all(IoMavenModule.class));
	}
	
	private void reloadUsages()
	{
		 List<IoMavenUsage> list = fMaven.allForParents(IoMavenUsage.class,developments);
		 EjbMavenUsageFactory.add(n2m,list);
		 
		 artifacts.addAll(n2m.toL1());
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