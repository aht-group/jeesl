package org.jeesl.factory.ejb.io.maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenUsage;
import org.jeesl.model.pojo.map.generic.Nested2Map;

public class EjbMavenUsageFactory
{
	public static IoMavenUsage build(IoMavenModule development, IoMavenVersion version)
	{
		IoMavenUsage ejb = new IoMavenUsage();
		ejb.setModule(development);
		ejb.setVersion(version);
		return ejb;
	}
	
	public static Set<IoMavenVersion> toSetVersion(List<IoMavenUsage> list)
	{
		Set<IoMavenVersion> set = new HashSet<>();
		for(IoMavenUsage usage : list) {set.add(usage.getVersion());}
		
		return set;
	}
	
	public static void add(Nested2Map<IoMavenArtifact,IoMavenModule,IoMavenUsage> n2m, List<IoMavenUsage> list)
	{
		for(IoMavenUsage ejb : list)
		{
			n2m.put(ejb.getVersion().getArtifact(),ejb.getModule(),ejb);
		}
	}
	
	public static Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> toMapArtifactRootModuleUsages(List<IoMavenUsage> list)
	{
		Map<IoMavenArtifact,Map<IoMavenModule,List<IoMavenVersion>>> map = new HashMap<>();
		for(IoMavenUsage ejb : list)
		{
			IoMavenArtifact a = ejb.getVersion().getArtifact();
			IoMavenModule m = null; if(Objects.nonNull(ejb.getModule().getParent())){m = ejb.getModule().getParent();} else {m = ejb.getModule();}
			
			if(!map.containsKey(a)) {map.put(a, new HashMap<>());}
			if(!map.get(a).containsKey(m)) {map.get(a).put(m,new ArrayList<>());}
			List<IoMavenVersion> l = map.get(a).get(m);
			if(!l.contains(ejb.getVersion())) {l.add(ejb.getVersion());}
		}
		return map;
	}
	
	public static Map<IoMavenVersion,List<IoMavenModule>> toMapVersionModules( List<IoMavenUsage> list)
	{
		Map<IoMavenVersion,List<IoMavenModule>> map = new HashMap<>();
		for(IoMavenUsage u : list)
		{
			if(!map.containsKey(u.getVersion())) {map.put(u.getVersion(),new ArrayList<>());}
			List<IoMavenModule> l = map.get(u.getVersion());
			if(!l.contains(u.getModule())) {l.add(u.getModule());}
		}
		return map;
	}
}