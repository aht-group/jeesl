package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.g.io.db.JeeslDbFlywayGwc;
import org.jeesl.factory.builder.io.db.IoDbFlywayFactoryBuilder;
import org.jeesl.interfaces.controller.web.io.db.JeeslIoDbFlywayCallback;
import org.jeesl.model.ejb.io.db.flyway.IoDbFlyway;
import org.jeesl.model.ejb.io.db.flyway.IoDbFlywayType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;

public class JeeslDbFlywayWc extends JeeslDbFlywayGwc<IoLang,IoDescription,IoLocale,IoDbFlyway,IoDbFlywayType>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbFlywayWc(JeeslIoDbFlywayCallback callback, IoDbFlywayFactoryBuilder<IoLang,IoDescription,IoDbFlyway,IoDbFlywayType> fb)
	{
		super(callback,fb);	
	}
}