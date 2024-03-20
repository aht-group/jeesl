package org.jeesl.controller.handler.rest.io.maven;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.ObjectUtils;
import org.exlp.controller.handler.io.log.LoggedExit;
import org.exlp.util.io.StringUtil;
import org.jeesl.api.facade.io.JeeslIoMavenFacade;
import org.jeesl.api.rest.i.io.JeeslIoMavenRestInterface;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.ejb.io.maven.EjbMavenDependencyFactory;
import org.jeesl.factory.ejb.io.maven.EjbMavenUsageFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.txt.io.maven.TxtMavenDependencyFactory;
import org.jeesl.factory.txt.io.maven.TxtMavenVersionFactory;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenOutdate;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenStructure;
import org.jeesl.interfaces.model.io.maven.classification.JeeslMavenSuitability;
import org.jeesl.interfaces.model.io.maven.usage.JeeslIoMavenUsage;
import org.jeesl.interfaces.util.query.io.JeeslIoMavenQuery;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenGroup;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenScope;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenSuitability;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.ee.IoMavenEeReferral;
import org.jeesl.model.ejb.io.maven.font.IoMavenFont;
import org.jeesl.model.ejb.io.maven.font.IoMavenFontUsage;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.model.ejb.io.ssi.core.IoSsiHost;
import org.jeesl.model.json.io.maven.JsonFont;
import org.jeesl.model.json.io.maven.JsonMavenArtifact;
import org.jeesl.model.json.io.maven.JsonMavenDependency;
import org.jeesl.model.json.io.maven.JsonMavenGraph;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.status.JsonScope;
import org.jeesl.util.comparator.ejb.io.maven.EjbMavenDependencyComparator;
import org.jeesl.util.db.cache.EjbCodeCache;
import org.jeesl.util.query.ejb.io.maven.EjbIoMavenQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JeeslIoMavenRestHandler implements JeeslIoMavenRestInterface
{
	public static final long serialVersionUID=1;
	final static Logger logger = LoggerFactory.getLogger(JeeslIoMavenRestHandler.class);
	
	private final JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven;
	
	private final EjbCodeCache<IoMavenStructure> cacheStructure;
	private final EjbCodeCache<IoMavenOutdate> cacheOutdate;
	private final EjbCodeCache<IoMavenSuitability> cacheSuitability;
	private final EjbCodeCache<IoMavenScope> cacheScope;
	
	public JeeslIoMavenRestHandler(JeeslIoMavenFacade<IoMavenGroup,IoMavenArtifact,IoMavenVersion,IoMavenDependency,IoMavenScope,IoMavenOutdate,IoMavenMaintainer,IoMavenModule,IoMavenStructure,IoMavenUsage,IoMavenEeReferral> fMaven)
	{
		this.fMaven=fMaven;
		
		cacheStructure = EjbCodeCache.instance(IoMavenStructure.class).facade(fMaven);
		cacheOutdate = EjbCodeCache.instance(IoMavenOutdate.class).facade(fMaven);
		cacheSuitability = EjbCodeCache.instance(IoMavenSuitability.class).facade(fMaven);
		cacheScope = EjbCodeCache.instance(IoMavenScope.class).facade(fMaven);
	}
	
	@Override public JsonSsiUpdate uploadDependencyGraph(JsonMavenGraph graph)
	{
		if(Objects.isNull(graph.getDependencies())) {graph.setDependencies(new ArrayList<>());}
		
		DataUpdateTracker dut = DataUpdateTracker.instance();

		IoMavenModule module = null;
		try
		{
			module = fMaven.fByCode(IoMavenModule.class, graph.getCode());
			if(cacheStructure.equals(module.getStructure(),JeeslMavenStructure.Code.mm)) {return dut.toJson();}
		}
		catch (JeeslNotFoundException e) {dut.error(e); return dut.toJson();}
		
		
		Map<IoMavenArtifact,List<IoMavenScope>> mapScope = new HashMap<>();
		List<IoMavenVersion> currentVersions = new ArrayList<>();
		Map<Long,IoMavenVersion> mapJsonId = new HashMap<>();
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
				
				List<IoMavenScope> scopes = new ArrayList<>();
				for(JsonScope scope : json.getScopes()) {scopes.add(cacheScope.ejb(scope.getCode()));}
				mapScope.put(artifact,scopes);
				
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
				mapJsonId.put(json.getId(), version);
			}
			catch (JeeslConstraintViolationException | JeeslLockingException e1)
			{
				dut.error(e1);
			}
		}
		
		logger.info(StringUtil.stars());
		logger.info(StringUtil.stars());
		
		List<IoMavenDependency> resolvedDependencies = new ArrayList<>();
		for(JsonMavenDependency json : graph.getDependencies())
		{
			IoMavenVersion vArtifact = mapJsonId.get(json.getFrom());
			IoMavenVersion vDependsOn = mapJsonId.get(json.getTo());
			
			if(vArtifact.getCode().contains("css")) {logger.warn(TxtMavenVersionFactory.full(vArtifact));}
			logger.warn(TxtMavenVersionFactory.full(vArtifact));
			
			IoMavenDependency tmpDependency = EjbMavenDependencyFactory.build(vArtifact, vDependsOn);
			EjbIdFactory.setNextNegativeId(tmpDependency,resolvedDependencies);
			resolvedDependencies.add(tmpDependency);
		}
		logger.info(StringUtil.stars());
		Collections.sort(resolvedDependencies,EjbMavenDependencyComparator.instance(EjbMavenDependencyComparator.Type.code));
		for(IoMavenDependency d : resolvedDependencies) {logger.info(TxtMavenDependencyFactory.build(d));}
		
		
		
		Map<IoMavenVersion,List<IoMavenVersion>> nowDependsOn = EjbMavenDependencyFactory.toMapDependsOn(resolvedDependencies);
		
		List<IoMavenDependency> dbDependencies = fMaven.fIoMavenDependencies(EjbIoMavenQuery.instance().addVersions(nowDependsOn.keySet()));
		Map<IoMavenVersion,List<IoMavenVersion>> dbDependsOn = EjbMavenDependencyFactory.toMapDependsOn(dbDependencies);
		Map<MultiKey<IoMavenVersion>,IoMavenDependency> mapDependencies = EjbMavenDependencyFactory.toMultiKeyMap(dbDependencies);
		
		logger.info(StringUtil.stars());
		logger.info(IoMavenDependency.class.getSimpleName()+".resolved: "+resolvedDependencies.size());
		logger.info(IoMavenDependency.class.getSimpleName()+".db: "+dbDependencies.size());
		logger.info(StringUtil.stars());
		
		for(IoMavenVersion v : nowDependsOn.keySet())
		{
			logger.info(TxtMavenVersionFactory.full(v));
			List<IoMavenVersion> lNow = new ArrayList<>(); if(nowDependsOn.containsKey(v)) {lNow.addAll(nowDependsOn.get(v));}
			List<IoMavenVersion> lDb = new ArrayList<>(); if(dbDependsOn.containsKey(v)) {lDb.addAll(dbDependsOn.get(v));}
			
			Collection<IoMavenVersion> cAdd = CollectionUtils.subtract(lNow,lDb);
			Collection<IoMavenVersion> cDel = CollectionUtils.subtract(lDb,lNow);
			
			if(ObjectUtils.isNotEmpty(cAdd) || ObjectUtils.isNotEmpty(cDel))
			{
				logger.info(TxtMavenVersionFactory.full(v));
				logger.info("\tcAdd "+cAdd.size());
				logger.info("\tcDel "+cDel.size());
				
				List<IoMavenDependency> addList = new ArrayList<>();
				for(IoMavenVersion dependsOn : cAdd) {addList.add(EjbMavenDependencyFactory.build(v,dependsOn));}
				
				List<IoMavenDependency> delList = new ArrayList<>();
				for(IoMavenVersion dependsOn : cDel) {delList.add(mapDependencies.get(new MultiKey<IoMavenVersion>(v,dependsOn)));}
				
				try
				{
					fMaven.save(addList);
					fMaven.rm(delList);
				}
				catch (JeeslConstraintViolationException | JeeslLockingException e) {e.printStackTrace();}
			}
		}

		JeeslIoMavenQuery<IoMavenVersion,IoMavenModule,IoMavenStructure> query = EjbIoMavenQuery.instance();
		query.addRootFetch(JeeslIoMavenUsage.Attributes.scopes);
		query.add(module);
	
		List<IoMavenUsage> usages = fMaven.fIoMavenUsages(query);
		Set<IoMavenVersion> existingVersions = EjbMavenUsageFactory.toSetVersion(usages);
		Map<IoMavenVersion,IoMavenUsage> existingUsages = EjbMavenUsageFactory.toMapVersion(usages);
		
		List<IoMavenUsage> newUsages = new ArrayList<>();
		List<IoMavenUsage> updatedUsages = new ArrayList<>();
		for(IoMavenVersion v : currentVersions)
		{
			if(existingVersions.contains(v))
			{
				IoMavenUsage u = existingUsages.get(v);
				if(Objects.isNull(u)) {logger.warn("NULL"); LoggedExit.exit(true);}
				
				Collection<IoMavenScope> listA = CollectionUtils.subtract(mapScope.get(v.getArtifact()),u.getScopes());
				Collection<IoMavenScope> listB = CollectionUtils.subtract(u.getScopes(),mapScope.get(v.getArtifact()));
				if(!listA.isEmpty() || listB.isEmpty())
				{
					u.setScopes(mapScope.get(v.getArtifact()));
					updatedUsages.add(u);
				}
				
				existingVersions.remove(v);
				existingUsages.remove(v);
			}
			else
			{
				IoMavenUsage usage = EjbMavenUsageFactory.build(module,v);
				usage.setScopes(mapScope.get(usage.getVersion().getArtifact()));
				newUsages.add(usage);
			}
		}
		
		try 
		{
			fMaven.save(newUsages);
			fMaven.save(updatedUsages);
			
			logger.info("Removing unneccessary usages: "+existingUsages.size());
			fMaven.rm(new ArrayList<>(existingUsages.values()));
			
//			logger.info("Removing unneccessary versions");
//			for(IoMavenVersion v : existingVersions)
//			{
//				List<IoMavenUsage> remaining = fMaven.allForParent(IoMavenUsage.class, JeeslIoMavenUsage.Attributes.version, v);
//				if(remaining.isEmpty())
//				{
//					fMaven.rm(v);
//				}
//			}
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