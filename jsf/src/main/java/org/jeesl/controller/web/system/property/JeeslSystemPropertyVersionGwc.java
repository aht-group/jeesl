package org.jeesl.controller.web.system.property;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import org.jeesl.api.facade.system.JeeslSystemPropertyFacade;
import org.jeesl.controller.web.AbstractJeeslLocaleWebController;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.PropertyFactoryBuilder;
import org.jeesl.interfaces.model.io.db.dump.JeeslDbDump;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.property.JeeslProperty;
import org.jeesl.interfaces.model.system.property.JeeslPropertyCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSystemPropertyVersionGwc <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											C extends JeeslPropertyCategory<L,D,C,?>,
											P extends JeeslProperty<L,D,C,P>>
		extends AbstractJeeslLocaleWebController<L,D,LOC>
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSystemPropertyVersionGwc.class);

	public JeeslSystemPropertyVersionGwc(final PropertyFactoryBuilder<L,D,C,P> fbProperty)
	{
		super(fbProperty.getClassL(),fbProperty.getClassD());
	}
	
	public LocalDateTime timeDatabase(JeeslSystemPropertyFacade<L,D,C,P> fProperty)
	{
		try
		{
			String backupTag = fProperty.valueStringForKey(JeeslDbDump.Tag.dbBackupTag.toString(),null);
			return LocalDateTime.parse(backupTag,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
		catch (JeeslNotFoundException e) {return null;}
	}
	
	public String timeZone()
	{
		ZoneId zone = ZoneId.systemDefault();
		return zone.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH);
	}
}