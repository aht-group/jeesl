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
	
	public static String xmlMaven(IoMavenVersion version)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t\t<dependency>").append("\n");
		sb.append("\t\t\t").append("<groupId>").append(version.getArtifact().getGroup().getCode()).append("</groupId>").append("\n");
		sb.append("\t\t\t").append("<artifactId>").append(version.getArtifact().getCode()).append("</artifactId>").append("\n");
		sb.append("\t\t\t").append("<version>").append(version.getCode()).append("</version>").append("\n");
		sb.append("\t\t\t").append("<scope>provided</scope>").append("\n");
		sb.append("\t\t</dependency>");
		
		return sb.toString();
	}
}