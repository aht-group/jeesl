package org.jeesl.factory.xml.dev.srs;

import java.util.List;

import org.jeesl.model.xml.dev.srs.Releases;
import org.jeesl.model.xml.dev.srs.Srs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReleasesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlReleasesFactory.class);
		
    public static Releases build()
    {
    	Releases xml = new Releases();

    	return xml;
    }
    
    public static Releases combine(List<Releases> list)
    {
    	if(list.isEmpty()){return build();}
    	else if(list.size()==1){return list.get(0);}
    	else
    	{
    		Releases xml = build();
    		xml.getReleases().addAll(list);
    		return xml;
    	}
    }
    
    public static Releases applySrs(Srs srs)
    {
    	srs.getReleases().setModule(srs.getCode());
    	return srs.getReleases();
    }
}