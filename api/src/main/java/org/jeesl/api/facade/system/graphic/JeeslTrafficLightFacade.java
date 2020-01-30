package org.jeesl.api.facade.system.graphic;

import java.util.List;

import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;

import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;

public interface JeeslTrafficLightFacade <L extends UtilsLang,D extends UtilsDescription,
											LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
											SCOPE extends UtilsStatus<SCOPE,L,D>>
					extends JeeslFacade
{	
	public List<LIGHT> allOrderedTrafficLights();
	List<LIGHT> allOrderedTrafficLights(SCOPE scope);
}