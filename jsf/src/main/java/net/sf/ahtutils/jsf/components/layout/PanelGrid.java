package net.sf.ahtutils.jsf.components.layout;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("net.sf.ahtutils.jsf.components.layout.PanelGrid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class PanelGrid extends UIPanel
{	
	final static Logger logger = LoggerFactory.getLogger(PanelGrid.class);
	public static enum Properties {width,gutter,styleClass}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	{
		if(event instanceof PostAddToViewEvent)
		{
			UIOutput css = new UIOutput();
			css.setRendererType("javax.faces.resource.Stylesheet");
			css.getAttributes().put("library", "ahtutilsCss");
			css.getAttributes().put("name", "panelGrid.css");
				
			FacesContext context = this.getFacesContext();
			context.getViewRoot().addComponentResource(context, css, "head");
		}
		super.processEvent(event);
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("class","auPanelGrid",null);
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
		ResponseWriter responseWriter = context.getResponseWriter();
		
		int rows = this.getChildCount()/2;
		if(this.getChildCount()%2!=0){rows=rows+1;}
		
		for(int row=0;row<rows;row++)
		{
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","auPanelGridRow",null);
			
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","auPgCell1",null);
			responseWriter.writeAttribute("style","min-width:20%;",null);
			this.getChildren().get(row*2).encodeAll(context);
			responseWriter.endElement("div");
			
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","auPgCell2",null);
			responseWriter.writeAttribute("style","width:80%;",null);
			this.getChildren().get(row*2+1).encodeAll(context);
			responseWriter.endElement("div");
			
			responseWriter.endElement("div");
/*			
			responseWriter.startElement("script", this);
			responseWriter.write(getScript());
			responseWriter.endElement("script");
*/		}
	}
}