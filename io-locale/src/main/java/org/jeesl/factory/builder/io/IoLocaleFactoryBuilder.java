package org.jeesl.factory.builder.io;

import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.model.ejb.io.graphic.core.IoGraphic;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicComponent;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicShape;
import org.jeesl.model.ejb.io.graphic.core.IoGraphicType;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;

public class IoLocaleFactoryBuilder
{
//	final static Logger logger = LoggerFactory.getLogger(IoLocaleFactoryBuilder.class);
	
	public static SvgFactoryBuilder<IoLang,IoDescription,IoGraphic,IoGraphicType,IoGraphicComponent,IoGraphicShape> svg() {return new SvgFactoryBuilder<>(IoLang.class,IoDescription.class,IoGraphic.class,IoGraphicType.class,IoGraphicComponent.class,IoGraphicShape.class);}

}