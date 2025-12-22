package org.jeesl.api.facade.system;

import java.util.Date;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.property.JeeslProperty;

public interface JeeslSystemPropertyFacade <L extends JeeslLang,D extends JeeslDescription,
											C extends JeeslStatus<L,D,C>,
											P extends JeeslProperty<L,D,C,P>>
			extends JeeslFacade
{
	public enum Code {dbIdentifier,devInstance}
	
	String valueStringForKey(String key, String defaultValue) throws JeeslNotFoundException;
	Integer valueIntForKey(String key, Integer defaultValue) throws JeeslNotFoundException;
	Long valueLongForKey(String key, Long defaultValue) throws JeeslNotFoundException;
	Boolean systemPropertyBoolean(String key, Boolean fallbackValue);
	Date valueDateForKey(String key, Date defaultValue) throws JeeslNotFoundException;
}