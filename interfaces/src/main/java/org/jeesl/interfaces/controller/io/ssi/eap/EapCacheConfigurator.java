package org.jeesl.interfaces.controller.io.ssi.eap;


import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;

public interface EapCacheConfigurator 
{
	void addCaches(JsonSsiSystem system) throws Exception;
}
