package org.jeesl.jsf.jk.component.html;

import java.io.IOException;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIPanel;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import org.jeesl.jsf.jk.util.ComponentAttribute;

@FacesComponent("org.jeesl.jsf.component.html.Li")
public class Li extends UIPanel
{	
	private static enum Properties {styleClass}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("li", this);
		
		responseWriter.writeAttribute("class",ComponentAttribute.get(Properties.styleClass, "", context, this),null);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("li");
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		for(UIComponent uic : this.getChildren())
		{
			uic.encodeAll(context);
		}
	}
}