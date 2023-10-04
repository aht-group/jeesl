package org.jeesl.interfaces.controller.io.ssi.eap;

import java.io.File;
import java.io.IOException;

import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;

public interface EapDsConfigurator 
{
	void setJbossRoot(File rootJboss);
	
	void addModule() throws IOException;
	void addDriver() throws IOException;
	void addDatasource(JsonSsiCredential json) throws IOException;
}
