package org.jeesl.api.facade.system.graphic;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;

public interface JeeslGraphicFacade <L extends JeeslLang, D extends JeeslDescription,
									S extends EjbWithId,
									G extends JeeslGraphic<L,D,GT,F,FS>, GT extends JeeslGraphicType<L,D,GT,G>,
									F extends JeeslGraphicComponent<L,D,G,GT,F,FS>, FS extends JeeslGraphicShape<L,D,FS,G>>
			extends JeeslFacade
{	
	G fGraphicForStatus(long statusId) throws JeeslNotFoundException;
	<W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, W w) throws JeeslNotFoundException;
	<W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, long id) throws JeeslNotFoundException; 
	<T extends EjbWithGraphic<G>> List<T> allWithGraphicFigures(Class<T> c);
}