package org.jeesl.interfaces.rest.system;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.model.xml.jeesl.Container;
import org.jeesl.model.xml.system.revision.Entity;

public interface JeeslSystemRestInterface <L extends JeeslLang, D extends JeeslDescription,
									R extends JeeslTenantRealm<L,D,R,?>,
									G extends JeeslGraphic<L,D,?,?,?>>
{	
	<X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.jeesl.Container exportStatus(String code) throws UtilsConfigurationException;
	<X extends JeeslStatus<L,D,X>> org.jeesl.model.xml.jeesl.Container updateTranslation(String code, Container xml) throws UtilsConfigurationException;
	Entity exportRevisionEntity(String code) throws UtilsConfigurationException;
	
}