package org.jeesl.interfaces.util.query.io;

import java.util.List;

import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicComponent;
import org.jeesl.interfaces.model.system.graphic.component.JeeslGraphicShape;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.util.query.JeeslCoreQuery;
import org.jeesl.interfaces.util.query.jpa.JeeslOrderingQuery;

public interface JeeslIoGraphicQuery<G extends JeeslGraphic<GT,GC,GS>,
									GT extends JeeslGraphicType<?,?,GT,G>,
									GC extends JeeslGraphicComponent<G,GC,GS>,
									GS extends JeeslGraphicShape<?,?,GS,G>>
					extends JeeslCoreQuery,JeeslOrderingQuery
{		
	List<GS> getIoGraphicShapes();
}