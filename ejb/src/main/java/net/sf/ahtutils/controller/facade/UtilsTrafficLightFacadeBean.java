package net.sf.ahtutils.controller.facade;

import java.util.List;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.system.graphic.JeeslTrafficLightFacade;
import org.jeesl.controller.facade.jx.JeeslFacadeBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;

public class UtilsTrafficLightFacadeBean <L extends JeeslLang,D extends JeeslDescription,
											LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
											SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
	extends JeeslFacadeBean implements JeeslTrafficLightFacade<L,D,LIGHT,SCOPE>
{	
	private static final long serialVersionUID = 1L;
	
	private final Class<LIGHT> cLight;
	
	public UtilsTrafficLightFacadeBean(EntityManager em, final Class<LIGHT> cLight)
	{
		super(em);
		this.cLight=cLight;
	}

	public List<LIGHT> allOrderedTrafficLights(SCOPE scope)
	{
		return this.allOrderedParent(cLight, JeeslTrafficLight.Attributes.threshold.toString(), true, "scope", scope);
	}
	
	public List<LIGHT> allOrderedTrafficLights()
	{
		return this.allOrdered(cLight, JeeslTrafficLight.Attributes.threshold, true);
	}
}