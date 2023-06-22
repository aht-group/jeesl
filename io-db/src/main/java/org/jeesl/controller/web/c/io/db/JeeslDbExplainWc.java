package org.jeesl.controller.web.c.io.db;

import org.jeesl.controller.web.g.io.db.JeeslDbExplainGwc;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;

public class JeeslDbExplainWc extends JeeslDbExplainGwc<IoLang,IoDescription,IoLocale>
{
	private static final long serialVersionUID = 1L;
	
	public JeeslDbExplainWc(IoLocaleFactoryBuilder<IoLang,IoDescription,IoLocale> fbCl)
	{
		super(fbCl);	
	}
}