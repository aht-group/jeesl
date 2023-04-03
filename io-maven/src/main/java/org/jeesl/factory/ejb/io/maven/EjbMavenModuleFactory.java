package org.jeesl.factory.ejb.io.maven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.model.ejb.io.maven.module.IoMavenModule;
import org.jeesl.model.ejb.io.maven.module.IoMavenStructure;
import org.jeesl.model.ejb.io.maven.module.IoMavenType;

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
		if(Objects.nonNull(ejb.getType())) {ejb.setType(facade.find(IoMavenType.class,ejb.getType()));}
	}
	
	public static Map<IoMavenType,List<IoMavenModule>> toMapModule(List<IoMavenModule> list)
	{
		Map<IoMavenType,List<IoMavenModule>> map = new HashMap<>();
		for(IoMavenModule ejb : list)
		{
			if(!map.containsKey(ejb.getType())) {map.put(ejb.getType(),new ArrayList<>());}
			map.get(ejb.getType()).add(ejb);
		}
		return map;
	}
}