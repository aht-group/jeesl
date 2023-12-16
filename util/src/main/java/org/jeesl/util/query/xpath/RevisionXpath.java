package org.jeesl.util.query.xpath;

import org.jeesl.model.xml.io.label.Entities;
import org.jeesl.model.xml.io.label.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class RevisionXpath
{
	final static Logger logger = LoggerFactory.getLogger(RevisionXpath.class);
	
	public static Entity getEntity(Entities xml, String code) throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		for(Entity e : xml.getEntity())
		{
			if(e.getCode().equals(code)) {return e;}
		}
		throw new ExlpXpathNotFoundException("No "+Entity.class.getSimpleName()+" for code="+code);
	}
	
}