package org.jeesl.factory.ejb.io.maven;

import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.classification.IoMavenDevelopmentType;
import org.jeesl.model.ejb.io.maven.usage.IoMavenModule;

public class EjbMavenModuleFactory
{
	public static IoMavenModule build()
	{
		IoMavenModule ejb = new IoMavenModule();
		
		return ejb;
	}
	
	public static void converter(JeeslFacade facade, IoMavenModule ejb)
	{
		if(Objects.nonNull(ejb.getType())) {ejb.setType(facade.find(IoMavenDevelopmentType.class,ejb.getType()));}
	}
}