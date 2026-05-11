package org.jeesl.controller.processor.io.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficLightProcessor <SCOPE extends JeeslTrafficLightScope<?,?,SCOPE,?>,
									LIGHT extends JeeslTrafficLight<?,?,SCOPE>
									>
{
	final static Logger logger = LoggerFactory.getLogger(TrafficLightProcessor.class);
	
	private final List<LIGHT> lights; public TrafficLightProcessor<SCOPE,LIGHT> lights(List<LIGHT> lights) {this.lights.clear(); this.lights.addAll(lights); return this;}
	
	public static <SCOPE extends JeeslTrafficLightScope<?,?,SCOPE,?>, LIGHT extends JeeslTrafficLight<?,?,SCOPE>>
		TrafficLightProcessor<SCOPE,LIGHT> instance()
	{
		return new TrafficLightProcessor<>();
	}
	private TrafficLightProcessor()
	{
		lights = new ArrayList<>();
	}
	
	public LIGHT find(int value)
	{
		return TrafficLightProcessor.findLight(lights, value);
	}
	public LIGHT find(double value)
	{
		return TrafficLightProcessor.findLight(lights, value);
	}
	
	public static <SCOPE extends JeeslTrafficLightScope<?,?,SCOPE,?>, LIGHT extends JeeslTrafficLight<?,?,SCOPE>>
		LIGHT findLight(List<LIGHT> lights, double value)
	{
		if(logger.isTraceEnabled()){logger.info("Loaded " + lights.size() + " Traffic Light Definitions from Application Scoped Bean.");}
		
		LIGHT result = null;
		for (LIGHT light : ListUtils.emptyIfNull(lights))
		{
			if(logger.isTraceEnabled()){logger.info("Comparing " +value +">=" +light.getThreshold());}
			if (value >= light.getThreshold())
			{
				result = light;
			}
		}
		return result;
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription, LIGHT extends JeeslTrafficLight<L,D,SCOPE>, SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
		String findScope(Object scopeAttribute)
	{
		String scope = "";
		if(logger.isTraceEnabled()){logger.info("scope has class " +scopeAttribute.getClass().toString());}
		
		if(scopeAttribute!=null)
		{
			if (scopeAttribute instanceof JeeslStatus)
			{
				@SuppressWarnings("unchecked")
				SCOPE scopeObj = (SCOPE) scopeAttribute;
				if(logger.isTraceEnabled()){logger.info("Scope is given as object" +scopeObj.getCode());}
				scope = scopeObj.getCode();
			}
			else
			{
				if(logger.isTraceEnabled()){logger.info("Scope is not given as Object. To String results in " +scopeAttribute.toString());}
				scope = scopeAttribute.toString();
			}
		}
		return scope;
	}
}