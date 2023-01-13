package org.jeesl.jsf.components.html;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.jsf.util.ComponentAttribute;

@FacesComponent("org.jeesl.jsf.components.html.Style")
public class Style extends UIPanel
{	
	private enum Properties {rules}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("style", this);
		
		responseWriter.write(ComponentAttribute.get(Properties.rules, "", context, this));
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("style");
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException { }
}