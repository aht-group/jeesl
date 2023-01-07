package org.jeesl.controller.handler.system.io.ssi;

import org.jeesl.model.json.io.ssi.core.JsonSsiContainer;
import org.jeesl.model.json.io.ssi.core.JsonSsiVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SsiVersionRestHandler
{
	final static Logger logger = LoggerFactory.getLogger(SsiVersionRestHandler.class);
	
	
	public SsiVersionRestHandler()
	{
		
	}
	
	public JsonSsiContainer versionsLibrary(JsonSsiContainer ssi)
	{
		if(ssi.getVersions()!=null)
		{
			for(JsonSsiVersion v : ssi.getVersions())
			{
				try {v.setVersion(Class.forName(v.getFqcn()).getPackage().getImplementationVersion());}
		    	catch (ClassNotFoundException e) {v.setVersion("--");}
			}
		}
		return ssi;
	}

}