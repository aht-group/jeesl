package org.jeesl.controller.handler.rest.io.maven;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.api.rest.i.io.JeeslIoMavenRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenSuitability;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.font.IoMavenFont;
import org.jeesl.model.ejb.io.maven.font.IoMavenFontUsage;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.json.io.maven.JsonFont;
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
	
	private final JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven;
	
	private final EjbCodeCache<IoMavenStructure> cacheStructure;
	private final EjbCodeCache<IoMavenOutdate> cacheOutdate;
	private final EjbCodeCache<IoMavenSuitability> cacheSuitability;
	
	public JeeslIoMavenRestHandler(JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenOutdate,IoMavenMaintainer,IoMavenStructure,IoMavenUsage> fMaven)
	{
		this.fMaven=fMaven;
		
		cacheStructure = EjbCodeCache.instance(IoMavenStructure.class).facade(fMaven);
		cacheOutdate = EjbCodeCache.instance(IoMavenOutdate.class).facade(fMaven);
		cacheSuitability = EjbCodeCache.instance(IoMavenSuitability.class).facade(fMaven);
	}
	
	@Override public JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph)
	{
		DataUpdateTracker dut = DataUpdateTracker.instance();

		IoMavenModule module = null;
		try
		{
			module = fMaven.fByCode(IoMavenModule.class, graph.getCode());
			
			if(cacheStructure.equals(module.getStructure(),JeeslMavenStructure.Code.mm)) {return dut.toJson();}
		}
		catch (JeeslNotFoundException e) {dut.error(e); return dut.toJson();}
		
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

		Set<IoMavenVersion> existingVersions = EjbMavenUsageFactory.toSetVersion(fMaven.allForParent(IoMavenUsage.class,module));
		List<IoMavenUsage> newUsages = new ArrayList<>();
		for(IoMavenVersion v : currentVersions)
		{
			if(existingVersions.contains(v)) {existingVersions.remove(v);}
			else {newUsages.add(EjbMavenUsageFactory.build(module,v));}
		}
		
		try 
		{
			fMaven.save(newUsages);
			fMaven.rm(existingVersions);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException e) {dut.fail(e,true); return dut.toJson();}
		
		return dut.toJson();
	}

	@Override public JsonSsiUpdate uploadFonts(JsonMavenGraph fonts)
	{
		DataUpdateTracker dut = DataUpdateTracker.instance();
		
		IoSsiHost host = null;
		Set<IoMavenFontUsage> set = new HashSet<>();
		try
		{
			host = fMaven.fByCode(IoSsiHost.class,fonts.getCode());
			set.addAll(fMaven.allForParent(IoMavenFontUsage.class,host));
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		for(JsonFont f : fonts.getFonts())
		{
			IoMavenFont font = null;
			try {font = fMaven.fByCode(IoMavenFont.class,f.getCode());}
			catch (JeeslNotFoundException e)
			{
				try
				{
					font = new IoMavenFont();
					font.setCode(f.getCode());
					font = fMaven.save(font);
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e1) {e1.printStackTrace();}
			}
			
			if(Objects.nonNull(host))
			{
				try
				{
					IoMavenFontUsage usage = new IoMavenFontUsage();
					usage.setHost(host);
					usage.setFont(font);
					fMaven.save(usage);
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return dut.toJson();
	}
}