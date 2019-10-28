package org.jeesl.factory.ejb.system.io.ssi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiAttribute;
import org.jeesl.interfaces.model.system.io.ssi.JeeslIoSsiMapping;

public class EjbIoSsiAttributeFactory <MAPPING extends JeeslIoSsiMapping<?,ENTITY>,
										ATTRIBUTE extends JeeslIoSsiAttribute<MAPPING,ENTITY>,
										ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
{
	private final Class<ATTRIBUTE> cAttriubte;

	public EjbIoSsiAttributeFactory(final Class<ATTRIBUTE> cAttriubte)
	{
        this.cAttriubte = cAttriubte;
	}
	
	public ATTRIBUTE build(MAPPING mapping)
	{
		ATTRIBUTE ejb = null;
		try
		{
			ejb = cAttriubte.newInstance();
			ejb.setMapping(mapping);
	       
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		return ejb;
	}
	
	public List<ENTITY> toListEntity(List<ATTRIBUTE> attributes)
	{
		Set<ENTITY> set = new HashSet<ENTITY>();
		for(ATTRIBUTE a : attributes) {set.add(a.getEntity());}
		return new ArrayList<>(set);
	}
}