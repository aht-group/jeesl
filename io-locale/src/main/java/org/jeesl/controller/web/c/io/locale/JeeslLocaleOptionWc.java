package org.jeesl.controller.web.c.io.locale;

import java.io.Serializable;

import org.jeesl.controller.web.io.locale.JeeslLocaleOptionController;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.interfaces.controller.web.io.label.JeeslOptionTableCallback;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicComponent;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslLocaleOptionWc <RE extends JeeslRevisionEntity<IoLang,IoDescription,?,?,?,?>>
									extends JeeslLocaleOptionController<IoLang,IoDescription,IoLocale,IoGraphic,IoGraphicType,IoGraphicComponent,IoGraphicShape,RE>
									implements Serializable//,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslLocaleOptionWc.class);
	
	public JeeslLocaleOptionWc(JeeslOptionTableCallback callback,
			IoLocaleFactoryBuilder<IoLang,IoDescription,IoLocale> fbStatus,
			SvgFactoryBuilder<IoLang,IoDescription,IoGraphic,IoGraphicType,IoGraphicComponent,IoGraphicShape> fbSvg,
			IoRevisionFactoryBuilder<IoLang,IoDescription,?,?,?,?,?,RE,?,?,?,?,?,?> fbRevision)
	{
		super(callback,fbStatus,fbSvg,fbRevision);
	}
}