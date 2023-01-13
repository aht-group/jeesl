package org.jeesl.jsf.components.html;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.jsf.util.ComponentAttribute;

@FacesComponent("org.jeesl.jsf.components.html.Label")
public class Label extends UIPanel
{	
	private static enum Properties {styleClass}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("label", this);
		
		responseWriter.writeAttribute("class",ComponentAttribute.get(Properties.styleClass, "", context, this),null);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("label");
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