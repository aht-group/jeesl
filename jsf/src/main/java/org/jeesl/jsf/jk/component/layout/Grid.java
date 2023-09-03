package org.jeesl.jsf.jk.component.layout;

import java.io.IOException;
import java.util.Map;

import org.jeesl.jsf.jk.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIOutput;
import jakarta.faces.component.UIPanel;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PostAddToViewEvent;

//@FacesComponent("org.jeesl.jsf.jk.component.layout.Grid")
@FacesComponent("org.jeesl.web.component.grid.Grid")
@ListenerFor(systemEventClass=PostAddToViewEvent.class)
public class Grid extends UIPanel
{	
	final static Logger logger = LoggerFactory.getLogger(Grid.class);
	protected static enum Properties {width,gutter,type,styleClass,renderChildren}

	protected int slot,gutter;
	
	public Grid()
	{
		super();
		slot=70;
		gutter=5;
	}
	
	protected void pushCssToHead() throws AbortProcessingException
	{
		StringBuffer sbCss = new StringBuffer();
		sbCss.append("grid-");
		sbCss.append(slot).append("-").append(gutter);
		sbCss.append(".css");
		this.pushCssToHead(sbCss.toString());
	}
	
	protected void pushCssToHead(String name)
	{
		this.pushCssToHead("ahtutilsCss", name);
	}
	
	protected void pushCssToHead(String library, String name)
	{
		UIOutput css = new UIOutput();
		css.setRendererType("jakarta.faces.resource.Stylesheet");
		css.getAttributes().put("library", library);
		css.getAttributes().put("name", name);
		
		FacesContext context = this.getFacesContext();
		context.getViewRoot().addComponentResource(context, css, "head");
	}

	@Override public boolean getRendersChildren() {return true;}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		if(ComponentAttribute.getBoolean(Properties.renderChildren,true,context,this))
		{
			for(UIComponent uic : this.getChildren())
			{
				uic.encodeAll(context);
			}
		}
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
			responseWriter.writeAttribute("class","aupContentCenter jeesl-content-center",null);
		}
		this.writeGridBegin(context, responseWriter, map);
	}
	protected void writeGridBegin(FacesContext context, ResponseWriter responseWriter, Map<String,Object> mapAttribute) throws IOException
	{
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");
		
		StringBuffer sbStyleClass = new StringBuffer();
		if(mapAttribute.containsKey(Properties.type.toString()))
		{
			String type = (String)mapAttribute.get(Properties.type.toString());
			if(type.equals("container")){sbStyleClass.append("auContainer");}
			else if(type.equals("op6")){sbStyleClass.append("auOp6Container");}
			else if(type.equals("op7")){sbStyleClass.append("auOp7Container");}
			else if(type.equals("op8")){sbStyleClass.append("auOp8Container");}
			else if(type.equals("op9")){sbStyleClass.append("auOp9Container");}
			else if(type.equals("op10")){sbStyleClass.append("auOp10Container");}
			else {logger.warn("Unknown Type");}
		}
		
		
		sbStyleClass.append(" jeesl-content-container");
		
		if(mapAttribute.containsKey(Properties.styleClass.toString()))
		{
			sbStyleClass.append(" ").append(mapAttribute.get(Properties.styleClass.toString()));
		}
		responseWriter.writeAttribute("class",sbStyleClass.toString().trim(),null);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		
		this.writeGridEnd(responseWriter);
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
	protected void writeGridEnd(ResponseWriter responseWriter) throws IOException
	{
		responseWriter.endElement("div");
	}
}