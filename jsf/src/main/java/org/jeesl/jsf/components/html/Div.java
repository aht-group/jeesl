package org.jeesl.jsf.components.html;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.jsf.jx.util.ComponentAttribute;

@FacesComponent("org.jeesl.jsf.components.html.Div")
public class Div extends UIPanel
{	
	private enum Properties {renderChildren,style,styleClass}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		Map<String,Object> map = super.getAttributes();
		
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		if(map.containsKey(Properties.style.toString()))
		{
			responseWriter.writeAttribute("style",map.get(Properties.style.toString()),null);
		}
		if(map.containsKey(Properties.styleClass.toString()))
		{
			responseWriter.writeAttribute("class",map.get(Properties.styleClass.toString()),null);
		}
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("div");
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
//		ResponseWriter responseWriter = context.getResponseWriter();
		if(ComponentAttribute.getBoolean(Properties.renderChildren, true, context, this))
		{
			for(UIComponent uic : this.getChildren())
			{
				uic.encodeAll(context);
			}
		}
	}
}