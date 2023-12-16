package org.jeesl.util.filter.xml.system.io;

import java.util.Iterator;
import java.util.List;

import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.io.label.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRevisionFilter
{
	final static Logger logger = LoggerFactory.getLogger(XmlRevisionFilter.class);
	
	public static Entities filterDocumentation(Entities entities){filter(entities.getEntity(),true,null);return entities;}
	public static Entities filter(Entities entities, Boolean documentation){filter(entities.getEntity(),documentation,null);return entities;}
	public static Entities filter(Entities entities, Boolean documentation, Boolean visible){filter(entities.getEntity(),documentation,visible);return entities;}
	
	private static void filter(List<Entity> entities, Boolean documentation, Boolean visible)
	{	
		Iterator<Entity> i = entities.iterator();
		while (i.hasNext())
		{
			Entity entity = i.next();
			
			if(documentation!=null && documentation!=BooleanComparator.active(entity.isDocumentation()))
	        {
	        	i.remove();
	        }
			else if(visible!=null && visible!=BooleanComparator.active(entity.isVisible()))
	        {
	        	i.remove();
	        }
	    }
	}
}