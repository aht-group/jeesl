package org.jeesl.factory.ejb.io.maven;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

public class EjbMavenDependencyFactory
{
	public static IoMavenDependency build(IoMavenVersion artifact, IoMavenVersion dependsOn)
	{
		IoMavenDependency ejb = new IoMavenDependency();
		ejb.setArtifact(artifact);
		ejb.setDependsOn(dependsOn);
		return ejb;
	}
	
	public static void converter(JeeslFacade facade, IoMavenDependency ejb)
	{
		
		if(Objects.nonNull(ejb.getArtifact())) {ejb.setArtifact(facade.find(IoMavenVersion.class,ejb.getArtifact()));}
		if(Objects.nonNull(ejb.getDependsOn())) {ejb.setDependsOn(facade.find(IoMavenVersion.class,ejb.getDependsOn()));}
	}
	
	public static List<IoMavenVersion> toListDependsOn(List<IoMavenDependency> list)
	{
		Set<IoMavenVersion> set = new HashSet<>();
		for(IoMavenDependency d : list)
		{
			set.add(d.getDependsOn());
		}
		return new ArrayList<>(set);
	}
	public static Map<IoMavenVersion,List<IoMavenVersion>> toMapDependsOn(List<IoMavenDependency> list)
	{
		Map<IoMavenVersion,List<IoMavenVersion>> map = new HashMap<>();
		for(IoMavenDependency d : list)
		{
			if(!map.containsKey(d.getArtifact())) {map.put(d.getArtifact(),new ArrayList<>());}
			map.get(d.getArtifact()).add(d.getDependsOn());
		}
		
		return map;
	}
	
	public static Map<MultiKey<IoMavenVersion>,IoMavenDependency> toMultiKeyMap(Collection<IoMavenDependency> list)
	{
		Map<MultiKey<IoMavenVersion>,IoMavenDependency> map = new HashMap<>();
		for(IoMavenDependency d : list)
		{
			MultiKey<IoMavenVersion> key = new MultiKey<IoMavenVersion>(d.getArtifact(),d.getDependsOn());
			map.put(key,d);
		}
		return map;
	}
	
	
}