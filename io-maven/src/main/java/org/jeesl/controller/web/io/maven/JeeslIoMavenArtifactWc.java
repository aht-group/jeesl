package org.jeesl.controller.web.io.maven;

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
import org.jeesl.factory.ejb.io.maven.EjbMavenVersionFactory;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.jsf.handler.sb.SbMultiHandler;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.io.maven.classification.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.classification.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.classification.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenArtifactComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public class JeeslIoMavenArtifactWc extends AbstractJeeslWebController<IoLang,IoDescription,IoLocale>
									implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenArtifactWc.class);
	
	private JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven;

	private final Comparator<IoMavenArtifact> cpArtifact;
	
	private final Map<IoMavenArtifact,List<IoMavenVersion>> mapVersion; public Map<IoMavenArtifact, List<IoMavenVersion>> getMapVersion() {return mapVersion;}
	
	private final List<IoMavenArtifact> artifacts; public List<IoMavenArtifact> getArtifacts() {return artifacts;}
	private final List<IoMavenVersion> versions; public List<IoMavenVersion> getVersions() {return versions;}
	private final List<IoMavenSuitability> suitabilities; public List<IoMavenSuitability> getSuitabilities() {return suitabilities;}
	private final List<IoMavenOutdate> outdates; public List<IoMavenOutdate> getOutdates() {return outdates;}
	private final List<IoMavenMaintainer> maintainers; public List<IoMavenMaintainer> getMaintainers() {return maintainers;}

	private IoMavenArtifact artifact; public IoMavenArtifact getArtifact() {return artifact;} public void setArtifact(IoMavenArtifact artifact) {this.artifact = artifact;}
	private IoMavenVersion version; public IoMavenVersion getVersion() {return version;} public void setVersion(IoMavenVersion version) {this.version = version;}
	
	public JeeslIoMavenArtifactWc()
	{
		super(IoLang.class,IoDescription.class);
		
		cpArtifact = EjbMavenArtifactComparator.instance(EjbMavenArtifactComparator.Type.code);
		
		mapVersion = new HashMap<>();
		
		artifacts = new ArrayList<>();
		versions = new ArrayList<>();
		suitabilities = new ArrayList<>();
		outdates = new ArrayList<>();
		maintainers = new ArrayList<>();
	}
	
	public void postConstruct(JeeslLocaleProvider<IoLocale> lp, JeeslFacesMessageBean bMessage,
							JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven)
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
	public void toggled(Class<?> c) throws JeeslLockingException, JeeslConstraintViolationException
	{
		if(debugOnInfo){logger.info(SbMultiHandler.class.getSimpleName()+" toggled, but NYI");}
	}
	
	private void reloadArtifacts()
	{
		artifacts.clear();
		artifacts.addAll(fMaven.all(IoMavenArtifact.class));
		logger.info(AbstractLogMessage.reloaded(IoMavenArtifact.class,artifacts));
		Collections.sort(artifacts,cpArtifact);
		
		List<IoMavenVersion> list = fMaven.all(IoMavenVersion.class);
		
		mapVersion.clear();
		mapVersion.putAll(EjbMavenVersionFactory.toMapArtifactVersion(list));
		logger.info(AbstractLogMessage.reloaded(IoMavenArtifact.class,artifacts)+" ("+mapVersion.size()+")");
	}
	
	public void selectArtifact()
	{
		this.reset(true, true);
		if(debugOnInfo) {logger.info(AbstractLogMessage.selectEntity(artifact));}
		this.reloadVersions();
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
}