package org.jeesl.factory.xml.system.io.ssi.docker;

import org.jeesl.model.xml.io.ssi.Docker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDockerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlDockerFactory.class);
	
	public static Docker build() {return new Docker();}
}