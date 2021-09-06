package org.jeesl.api.bean;

import java.util.List;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;

public interface JeeslTrafficLightBean<L extends JeeslLang,D extends JeeslDescription,
										LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
										SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
{	
	List<LIGHT> getTrafficLights(String scope);
	
	void refreshTrafficLights();
}