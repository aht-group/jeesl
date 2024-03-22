package org.jeesl.interfaces.controller.web.io.maven;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.io.maven.module.JeeslMavenType;

public interface JeeslIoMavenModuleCallback <TYPE extends JeeslMavenType<?,?,?,?>> extends Serializable
{
	List<TYPE> getTypes();
}