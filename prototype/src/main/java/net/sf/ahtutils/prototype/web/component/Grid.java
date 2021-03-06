package net.sf.ahtutils.prototype.web.component;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

import net.sf.ahtutils.jsf.components.layout.AbstractUtilsGrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("net.sf.ahtutils.prototype.web.component.Grid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class Grid extends AbstractUtilsGrid
{	
	final static Logger logger = LoggerFactory.getLogger(Grid.class);
	
	public Grid()
	{
		super();
		slot=70;
		gutter=5;
	}
	
	@Override
	public void processEvent(ComponentSystemEvent event) throws AbortProcessingException
	{
		if(event instanceof PostAddToViewEvent)
		{
			if(event.getComponent().getFacets().containsKey("left")) {slot=slot-15;}
			if(event.getComponent().getFacets().containsKey("right")) {slot=slot-15;}
					
//			super.pushCssToHead();
		 }
		super.processEvent(event);
	}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		Map<String,Object> map = getAttributes();
		
		ResponseWriter responseWriter = context.getResponseWriter();
		if(this.getFacets().containsKey("left"))
		{
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","aupContentLeft",null);
			this.getFacet("left").encodeAll(context);
			responseWriter.endElement("div");
		}
		
		if(this.getFacets().containsKey("left") || this.getFacets().containsKey("right"))
		{
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","aupContentCenter jeesl-content",null);
		}
		super.writeGridBegin(context, responseWriter, map);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		
		super.writeGridEnd(responseWriter);
		if(this.getFacets().containsKey("left") || this.getFacets().containsKey("right"))
		{
			responseWriter.endElement("div");
		}
		
		if(this.getFacets().containsKey("right"))
		{
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","aupContentRight jeesl-content-right",null);
			this.getFacet("right").encodeAll(context);
			responseWriter.endElement("div");
		}
	}
}