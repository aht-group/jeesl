package org.jeesl.controller.web.c.system.property;

import org.jeesl.controller.web.system.JeeslSystemPropertyGwc;
import org.jeesl.factory.builder.system.PropertyFactoryBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.property.SystemProperty;
import org.jeesl.model.ejb.system.property.SystemPropertyCategory;

public class JeeslSystemPropertyWc extends JeeslSystemPropertyGwc<IoLang,IoDescription,IoLocale,SystemPropertyCategory,SystemProperty>
{
	private static final long serialVersionUID = 1L;

	public JeeslSystemPropertyWc(final PropertyFactoryBuilder<IoLang,IoDescription,SystemPropertyCategory,SystemProperty> fbProperty)
	{
		super(fbProperty);
	}
}