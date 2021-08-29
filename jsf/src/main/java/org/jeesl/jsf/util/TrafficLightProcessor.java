package org.jeesl.jsf.util;

import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.jeesl.api.bean.JeeslTrafficLightBean;
import org.jeesl.factory.css.CssColorFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficLightProcessor
{
	final static Logger logger = LoggerFactory.getLogger(TrafficLightProcessor.class);
	private static enum Attribute {scope,style}
	
	public static <L extends JeeslLang,D extends JeeslDescription,
					LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
					SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
		LIGHT findLight(List<LIGHT> lights, double value)
	{
		if(logger.isTraceEnabled()){logger.info("Loaded " + lights.size() + " Traffic Light Definitions from Application Scoped Bean.");}
		
		LIGHT result = null;
		if(lights!=null)
		{
			for (LIGHT light : lights)
			{
				if(logger.isTraceEnabled()){logger.info("Comparing " +value +">=" +light.getThreshold());}
				if (value >= light.getThreshold())
				{
					result = light;
				}
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
	
	public static <L extends JeeslLang,D extends JeeslDescription, LIGHT extends JeeslTrafficLight<L,D,SCOPE>, SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
		void appendStyle(StringBuilder sb, FacesContext context, Map<String,Object> map, double value, Double ref)
	{
		if(ref!=null)
		{
			if(ref!=0) {value = (value/ref)*100;}
			else if(value==0) {value=100;}
		}
		appendStyle(sb,context,map,value);
	}
	
	public static <L extends JeeslLang,D extends JeeslDescription, LIGHT extends JeeslTrafficLight<L,D,SCOPE>, SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
		void appendStyle(StringBuilder sb, FacesContext context, Map<String,Object> map, double value)
	{
		@SuppressWarnings("unchecked")
		JeeslTrafficLightBean<L,D,LIGHT,SCOPE> appBean = (JeeslTrafficLightBean<L,D,LIGHT,SCOPE>) context.getApplication().evaluateExpressionGet(context, "#{appTrafficLightsBean}", JeeslTrafficLightBean.class);
       
        String scope = TrafficLightProcessor.findScope(map.get(Attribute.scope.toString()));			
		CssColorFactory.appendColor(sb, TrafficLightProcessor.findLight(appBean.getTrafficLights(scope), value));
	}
}