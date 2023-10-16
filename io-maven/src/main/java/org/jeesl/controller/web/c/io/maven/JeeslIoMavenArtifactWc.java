package org.jeesl.controller.web.c.io.maven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.controller.web.AbstractJeeslWebController;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenVersionFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.bean.sb.handler.SbToggleSelection;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.jeesl.util.query.ejb.io.maven.JeeslIoMavenQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenArtifactWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenArtifactWc.class);
	
	private JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven;

	private final Comparator<IoMavenArtifact> cpArtifact;
	private final Comparator<IoMavenVersion> cpVersion;
	
	private final Map<IoMavenArtifact,List<IoMavenVersion>> mapVersion; public Map<IoMavenArtifact, List<IoMavenVersion>> getMapVersion() {return mapVersion;}
	private final Map<IoMavenVersion,List<IoMavenModule>> mapRoot; public Map<IoMavenVersion,List<IoMavenModule>> getMapRoot() {return mapRoot;}
	private final Map<IoMavenVersion,Map<IoMavenModule,List<IoMavenUsage>>> mapUsage; public Map<IoMavenVersion,Map<IoMavenModule,List<IoMavenUsage>>> getMapUsage() {return mapUsage;}
	
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenVersion> versions; public List<IoMavenVersion> getVersions() {return versions;}
	private final List<IoMavenSuitability> suitabilities; public List<IoMavenSuitability> getSuitabilities() {return suitabilities;}
	private final List<IoMavenOutdate> outdates; public List<IoMavenOutdate> getOutdates() {return outdates;}
	private final List<IoMavenMaintainer> maintainers; public List<IoMavenMaintainer> getMaintainers() {return maintainers;} 
	
	private IoMavenArtifact artifact; public IoMavenArtifact getArtifact() {return artifact;} public void setArtifact(IoMavenArtifact artifact) {this.artifact = artifact;}
	private IoMavenVersion version; public IoMavenVersion getVersion() {return version;} public void setVersion(IoMavenVersion version) {this.version = version;}
	
	private String fvGroup; public String getFvGroup() {return fvGroup;} public void setFvGroup(String fvGroup) {this.fvGroup = fvGroup;}
	private String fvArtifact; public String getFvArtifact() {return fvArtifact;} public void setFvArtifact(String fvArtifact) {this.fvArtifact = fvArtifact;}
	
	public JeeslIoMavenArtifactWc()
	{
		super(IoLang.class,IoDescription.class);
		
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		cpVersion = new PositionComparator<>();
		
		mapVersion = new HashMap<>();
		mapRoot = new HashMap<>();
		mapUsage = new HashMap<>();
		
		artifacts = new ArrayList<>();
		versions = new ArrayList<>();
		suitabilities = new ArrayList<>();
		outdates = new ArrayList<>();
		maintainers = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage> fMaven)
	{
		super.postConstructWebController(lp,bMessage);
		this.fMaven=fMaven;

		suitabilities.addAll(fMaven.allOrderedPositionVisible(IoMavenSuitability.class));
		outdates.addAll(fMaven.allOrderedPositionVisible(IoMavenOutdate.class));
		maintainers.addAll(fMaven.allOrderedPositionVisible(IoMavenMaintainer.class));
		
		this.reloadArtifacts();
	}
	
	public void cancelVersion() {this.reset(false, true);}
	private void reset(boolean rVersions, boolean rVersion)
	{
		if(rVersions) {versions.clear();}
		if(rVersion) {version=null;}
	}
	
	@Override
	public void toggled(SbToggleSelection handler, Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	private void reloadArtifacts()
	{
		List<IoMavenVersion> list = fMaven.all(IoMavenVersion.class);
		Collections.sort(list,cpVersion);
		mapVersion.clear();
		mapVersion.putAll(EjbMavenVersionFactory.toMapArtifactVersion(list));
		
		artifacts.clear();
		artifacts.addAll(mapVersion.keySet());
		Collections.sort(artifacts,cpArtifact);
		logger.info(AbstractLogMessage.reloaded(IoMavenArtifact.class,artifacts));
	}
	
	public void selectArtifact()
	{
		this.reset(true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(artifact));}
		this.reloadVersions();
		this.reloadUsages();
	}
	
	public void saveArtifact() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(artifact));}
		artifact = fMaven.save(artifact);
		
		reloadArtifacts();
	}
	
	
	private void reloadVersions()
	{
		this.reset(true,false);
		versions.addAll(fMaven.allForParent(IoMavenVersion.class,artifact));
	}
	
	public void selectVersion() throws JeeslNotFoundException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(version));}
	}
	
	public void saveVersion() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		if(debugOnInfo) {logger.info(AbstractLogMessage.saveEntity(version));}
		EjbMavenVersionFactory.converter(fMaven, version);
		version = fMaven.save(version);
		
		this.reloadVersions();
		mapVersion.remove(version.getArtifact());
		mapVersion.putAll(EjbMavenVersionFactory.toMapArtifactVersion(versions));
	}
	
	private void reloadUsages()
	{
		mapRoot.clear();
		mapUsage.clear();
		
		List<IoMavenUsage> usages = fMaven.fIoMavenUsages(JeeslIoMavenQuery.instance().addVersions(versions).addRootFetch(JeeslIoMavenUsage.Attributes.scopes));
		mapRoot.putAll(EjbMavenUsageFactory.toMapVersionRootModules(usages));
		mapUsage.putAll(EjbMavenUsageFactory.toMapVersionModuleUsage(usages));
	}
	
	public void reorderVersions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fMaven,versions); this.reloadVersions();}
}