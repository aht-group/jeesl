package org.jeesl.interfaces.controller.io.ssi.eap;


import java.io.IOException;

public interface EapLoggingConfigurator 
{
	void addLogger(String name, String level, String formatter) throws IOException;
}
