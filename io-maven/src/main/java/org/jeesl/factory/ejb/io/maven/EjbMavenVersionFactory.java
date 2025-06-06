package org.jeesl.factory.ejb.io.maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenArtifact;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenMaintainer;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenOutdate;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;
import org.jeesl.model.ejb.io.maven.module.IoMavenJdk;

public class EjbMavenVersionFactory
{
	public static IoMavenVersion build()
	{
		IoMavenVersion ejb = new IoMavenVersion();
		
		return ejb;
	}
	
	public static Map<IoMavenArtifact,List<IoMavenVersion>> toMapArtifactVersion(List<IoMavenVersion> list)
	{
		Map<IoMavenArtifact,List<IoMavenVersion>> map = new HashMap<>();
		for(IoMavenVersion v : list)
		{
			if(!map.containsKey(v.getArtifact())) {map.put(v.getArtifact(),new ArrayList<>());}
			map.get(v.getArtifact()).add(v);
		}
		
		return map;
	}
}