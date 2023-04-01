package org.jeesl.factory.ejb.io.maven;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;
import org.jeesl.model.ejb.io.maven.usage.IoMavenUsage;
import org.jeesl.model.pojo.map.generic.Nested2Map;

public class EjbMavenUsageFactory
{
	public static IoMavenUsage build(IoMavenModule development, IoMavenVersion version)
	{
		IoMavenUsage ejb = new IoMavenUsage();
		ejb.setDevelopment(development);
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
			n2m.put(ejb.getVersion().getArtifact(),ejb.getDevelopment(),ejb);
		}
	}
}