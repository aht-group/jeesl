package org.jeesl.factory.txt.io.maven;

import org.jeesl.model.ejb.io.maven.dependency.IoMavenDependency;
import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

public class TxtMavenDependencyFactory
{
	public static String build(IoMavenDependency dependency)
	{
		return TxtMavenDependencyFactory.build(dependency.getArtifact(),dependency.getDependsOn());
	}
	
	public static String build(IoMavenVersion artifact, IoMavenVersion dependsOn)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(TxtMavenVersionFactory.full(artifact));
		sb.append(" depends on ");
		sb.append(TxtMavenVersionFactory.full(dependsOn));
		
		
		return sb.toString();
	}
}