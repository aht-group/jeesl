package org.jeesl.api.facade.io;

import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.graphic.EjbWithGraphic;
import org.jeesl.interfaces.util.query.io.JeeslIoGraphicQuery;

public interface JeeslIoGraphicFacade <S extends EjbWithId,
										G extends JeeslGraphic<GT,GC,GS>, GT extends JeeslGraphicType<?,?,GT,G>,
										GC extends JeeslGraphicComponent<G,GC,GS>, GS extends JeeslGraphicShape<?,?,GS,G>>
			extends JeeslFacade
{	
	G fGraphicForStatus(long statusId) throws JeeslNotFoundException;
	<W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, W w) throws JeeslNotFoundException;
	<W extends EjbWithGraphic<G>> G fGraphic(Class<W> c, long id) throws JeeslNotFoundException; 
	<T extends EjbWithGraphic<G>> List<T> allWithGraphicFigures(Class<T> c);
	
	<T extends EjbWithGraphic<G>> List<GC> fIoGraphicComponents(Class<T> c, JeeslIoGraphicQuery<G,GT,GC,GS> query);
}