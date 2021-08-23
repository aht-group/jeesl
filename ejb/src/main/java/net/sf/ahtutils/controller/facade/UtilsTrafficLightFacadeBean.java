package net.sf.ahtutils.controller.facade;

import java.util.List;

import javax.persistence.EntityManager;

import org.jeesl.api.facade.system.graphic.JeeslTrafficLightFacade;
import org.jeesl.controller.facade.JeeslFacadeBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;

public class UtilsTrafficLightFacadeBean <L extends JeeslLang,D extends JeeslDescription,
											LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
											SCOPE extends JeeslStatus<L,D,SCOPE>>
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
		return this.allOrderedParent(cLight, "threshold", true, "scope", scope);
	}
	
	public List<LIGHT> allOrderedTrafficLights()
	{
		return this.allOrdered(cLight, "threshold", true);
	}
}