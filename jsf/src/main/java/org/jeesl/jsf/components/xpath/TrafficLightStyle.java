package org.jeesl.jsf.components.xpath;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.jeesl.api.bean.JeeslTrafficLightBean;
import org.jeesl.controller.processor.io.report.TrafficLightProcessor;
import org.jeesl.factory.css.CssColorFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficLightStyle
{
	final static Logger logger = LoggerFactory.getLogger(TrafficLightStyle.class);
	private static enum Attribute {scope,style}
	
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