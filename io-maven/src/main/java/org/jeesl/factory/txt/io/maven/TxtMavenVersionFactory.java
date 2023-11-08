package org.jeesl.factory.txt.io.maven;

import org.jeesl.model.ejb.io.maven.dependency.IoMavenVersion;

public class TxtMavenVersionFactory
{
	public static String full(IoMavenVersion version)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(version.getArtifact().getGroup().getCode());
		sb.append(":").append(version.getArtifact().getCode());
		sb.append(":").append(version.getCode());
		
		
		return sb.toString();
	}
}