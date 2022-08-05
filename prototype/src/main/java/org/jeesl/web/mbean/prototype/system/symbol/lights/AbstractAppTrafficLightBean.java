package org.jeesl.web.mbean.prototype.system.symbol.lights;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jeesl.api.bean.JeeslTrafficLightBean;
import org.jeesl.api.facade.system.graphic.JeeslTrafficLightFacade;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractAppTrafficLightBean <L extends JeeslLang,D extends JeeslDescription,
										LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
										SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
					implements Serializable,JeeslTrafficLightBean<L,D,LIGHT,SCOPE>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAppTrafficLightBean.class);
	
	private JeeslTrafficLightFacade<L,D,LIGHT,SCOPE> fLight;
		
	private Hashtable<String,List<LIGHT>> cache;
	
	protected void init(JeeslTrafficLightFacade<L,D,LIGHT,SCOPE> fLight)
	{
		this.fLight=fLight;
	}
	
	protected List<LIGHT> trafficLights; public List<LIGHT> getTrafficLights(){return trafficLights;}
	
	@Override public void refreshTrafficLights()
	{
	    cache = new Hashtable<String,List<LIGHT>>();
	    
	    trafficLights = fLight.allOrderedTrafficLights();
	    for (LIGHT light : trafficLights)
	    {
	    		SCOPE scope = light.getScope();
			if (cache.containsKey(scope.getCode()))
			{
			    cache.get(scope.getCode()).add(light);
			}
			else
			{
			    ArrayList<LIGHT> scopeLights = new ArrayList<LIGHT>();
			    scopeLights.add(light);
			    cache.put(scope.getCode(), scopeLights);
			}
	    }
	}
	
	@Override public List<LIGHT> getTrafficLights(String scope)
	{
	    if (cache == null) {refreshTrafficLights();}
	    if(scope==null || scope.length()==0 || !cache.containsKey(scope)) {return null;}
	    if(logger.isTraceEnabled()){logger.info("Found " +cache.get(scope).size() +" lights definitions in scope " +scope);}
	    return cache.get(scope);
	}
}