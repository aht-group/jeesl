package org.jeesl.factory.ejb.io.maven;

import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.classification.IoMavenStructure;
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
		if(Objects.nonNull(ejb.getStructure())) {ejb.setStructure(facade.find(IoMavenStructure.class,ejb.getStructure()));}
	}
}