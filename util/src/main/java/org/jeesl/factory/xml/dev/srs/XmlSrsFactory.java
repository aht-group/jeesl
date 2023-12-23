package org.jeesl.factory.xml.dev.srs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jeesl.model.xml.module.dev.srs.Actors;
import org.jeesl.model.xml.module.dev.srs.Frs;
import org.jeesl.model.xml.module.dev.srs.Srs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlSrsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlSrsFactory.class);
	
	public enum Chapter {fr}
	
    public static Srs build() {return new Srs(); }
    
    public static Srs build(Frs frs) {Srs xml = build(); xml.setFrs(frs);return xml;}
    
    
    public static Srs combine(List<Srs> list)
    {
    	List<Actors> actors = new ArrayList<Actors>();
    	for(Srs srs : list)
    	{
    		if(Objects.nonNull(srs.getActors()) && !srs.getActors().getActor().isEmpty()) {actors.add(XmlActorsFactory.applySrs(srs));}
    	}
    	Srs xml = build();
    	xml.setActors(XmlActorsFactory.combine(actors));
    	
    	return xml;
    }
}