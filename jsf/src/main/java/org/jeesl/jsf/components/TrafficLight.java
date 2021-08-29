package org.jeesl.jsf.components;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.jeesl.factory.css.CssAlignmentFactory;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLight;
import org.jeesl.interfaces.model.system.util.JeeslTrafficLightScope;
import org.jeesl.jsf.util.ComponentAttributeProcessor;
import org.jeesl.jsf.util.TrafficLightProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.TrafficLight")
public class TrafficLight <L extends JeeslLang,D extends JeeslDescription,
							LIGHT extends JeeslTrafficLight<L,D,SCOPE>,
							SCOPE extends JeeslTrafficLightScope<L,D,SCOPE,?>>
							extends UIOutput 
{
    final static Logger logger = LoggerFactory.getLogger(TrafficLight.class);
	
    private static enum Attribute {value,scope,style}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		context.getResponseWriter().startElement("div", this);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		context.getResponseWriter().endElement("div");
	}

	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		StringBuilder sbStyle = new StringBuilder();
		ComponentAttributeProcessor.defaultStyle(sbStyle, this.getAttributes());
		CssAlignmentFactory.appendTextCenter(sbStyle);
		
		Object o =  this.getAttributes().get(Attribute.value.toString());
		
		String s = null; 
		Double value = null;
		if(o instanceof Double) {value=(Double)o;s=value.toString();}
		else if(o instanceof Integer)
		{
			Integer i = (Integer)o;
			s = i.toString();
			value = i.doubleValue();
		}
		
		if (value!=null)
		{
			TrafficLightProcessor.appendStyle(sbStyle,context,this.getAttributes(),value);
			if(sbStyle.length()>0) {context.getResponseWriter().writeAttribute(Attribute.style.toString(),sbStyle.toString(), null);}
			context.getResponseWriter().write(s);
		}
	}
}