package org.jeesl.api.rest.i.system;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.xml.io.label.Entity;
import org.jeesl.model.xml.xsd.Container;

public interface JeeslSystemRestInterface <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									G extends JeeslGraphic<?,?,?>>
{	
	<X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.xsd.Container exportStatus(String code) throws UtilsConfigurationException;
	<X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.xsd.Container updateTranslation(String code, Container xml) throws UtilsConfigurationException;
	Entity exportRevisionEntity(String code) throws UtilsConfigurationException;
	
}