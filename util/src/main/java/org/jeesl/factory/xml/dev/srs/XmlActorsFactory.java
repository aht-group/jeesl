package org.jeesl.factory.xml.dev.srs;

import java.util.List;

import org.jeesl.model.xml.dev.srs.Actors;
import org.jeesl.model.xml.dev.srs.Srs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlActorsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlActorsFactory.class);
		
    public static Actors build()
    {
    	Actors xml = new Actors();

    	return xml;
    }
    
    public static Actors combine(List<Actors> list)
    {
    	if(list.isEmpty()){return build();}
    	else if(list.size()==1){return list.get(0);}
    	else
    	{
    		Actors xml = build();
    		xml.getActors().addAll(list);
    		return xml;
    	}
    }
    
    public static Actors applySrs(Srs srs)
    {
    	srs.getActors().setModule(srs.getCode());
    	return srs.getActors();
    }
}