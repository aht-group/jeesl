package org.jeesl.controller.handler.rest.io.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.api.rest.i.io.maven.JeeslIoMavenRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenSuitability;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.maven.classification.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.classification.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.model.json.io.maven.JsonMavenArtifact;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JeeslIoMavenRestHandler implements JeeslIoMavenRestInterface
{
	public static final long serialVersionUID=1;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenRestHandler.class);
	
	private final JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven;
	
	private final EjbCodeCache<IoMavenOutdate> cacheOutdate;
	private final EjbCodeCache<IoMavenSuitability> cacheSuitability;
	
	public JeeslIoMavenRestHandler(JeeslIoMavenFacade<IoLang,IoDescription,IoMavenGroup,IoMavenArtifact,IoMavenVersion> fMaven)
	{
		this.fMaven=fMaven;
		
		cacheOutdate = new EjbCodeCache<>(IoMavenOutdate.class,fMaven);
		cacheSuitability = new EjbCodeCache<>(IoMavenSuitability.class,fMaven);
	}
	
	@Override public JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph)
	{
		DataUpdateTracker dut = DataUpdateTracker.instance();

		List<IoMavenVersion> currentVersions = new ArrayList<>();
		for(JsonMavenArtifact json : graph.getArtifacts())
		{
			logger.info(json.getGroupId()+":"+json.getArtifactId());
			try
			{
				IoMavenGroup group = null;
				try {group = fMaven.fByCode(IoMavenGroup.class, json.getGroupId());}
				catch (JeeslNotFoundException e)
				{
					group = new IoMavenGroup();
					group.setCode(json.getGroupId());
					group = fMaven.save(group);
				}
				
				IoMavenArtifact artifact = null;
				try {artifact = fMaven.fIoMavenArtifact(group, json.getArtifactId());}
				catch (JeeslNotFoundException e)
				{
					artifact = new IoMavenArtifact();
					artifact.setGroup(group);
					artifact.setCode(json.getArtifactId());
					artifact.setSuitability(cacheSuitability.ejb(JeeslMavenSuitability.Code.unknown));
					artifact = fMaven.save(artifact);
				}
				
				IoMavenVersion version = null;
				try {version = fMaven.fIoMavenVersion(artifact, json.getVersion());}
				catch (JeeslNotFoundException e)
				{
					version = new IoMavenVersion();
					version.setArtifact(artifact);
					version.setCode(json.getVersion());
					version.setOutdate(cacheOutdate.ejb(JeeslMavenOutdate.Code.unknown));
					version = fMaven.save(version);
				}
				currentVersions.add(version);
				
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e1)
			{
				dut.error(e1);
			}
		}
		
		IoMavenModule development = null;
		try{development = fMaven.fByCode(IoMavenModule.class, graph.getCode());}
		catch (JeeslNotFoundException e) {dut.error(e); return dut.toJson();}

		Set<IoMavenVersion> existingVersions = EjbMavenUsageFactory.toSetVersion(fMaven.allForParent(IoMavenUsage.class,development));
		List<IoMavenUsage> newUsages = new ArrayList<>();
		for(IoMavenVersion v : currentVersions)
		{
			if(existingVersions.contains(v)) {existingVersions.remove(v);}
			else {newUsages.add(EjbMavenUsageFactory.build(development,v));}
		}
		
		try 
		{
			fMaven.save(newUsages);
			fMaven.rm(existingVersions);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {dut.fail(e,true); return dut.toJson();}
		
		return dut.toJson();
	}
}