package org.jeesl.jsf.components.layout.grid;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;

import org.jeesl.jsf.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractGrid extends UIPanel
{	
	final static Logger logger = LoggerFactory.getLogger(AbstractGrid.class);
	protected static enum Properties {width,gutter,type,styleClass,renderChildren}

	protected int slot,gutter;
	
	public AbstractGrid()
	{	
		slot=70;
		gutter=5;
	}
	
	protected void pushCssToHead() throws AbortProcessingException
	{
		StringBuffer sbCss = new StringBuffer();
		sbCss.append("grid-");
		sbCss.append(slot).append("-").append(gutter);
		sbCss.append(".css");
		pushCssToHead(sbCss.toString());
	}
	
	protected void pushCssToHead(String name)
	{
		pushCssToHead("ahtutilsCss", name);
	}
	
	protected void pushCssToHead(String library, String name)
	{
		UIOutput css = new UIOutput();
		css.setRendererType("javax.faces.resource.Stylesheet");
		css.getAttributes().put("library", library);
		css.getAttributes().put("name", name);
		
		FacesContext context = this.getFacesContext();
		context.getViewRoot().addComponentResource(context, css, "head");
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
		else {sbStyleClass.append("auContainer");}
		
		sbStyleClass.append(" jeesl-content-container");
		
		if(mapAttribute.containsKey(Properties.styleClass.toString()))
		{
			sbStyleClass.append(" ").append(mapAttribute.get(Properties.styleClass.toString()));
		}
		responseWriter.writeAttribute("class",sbStyleClass.toString(),null);
	}
	
	protected void writeGridEnd(ResponseWriter responseWriter) throws IOException
	{
		responseWriter.endElement("div");
	}
	
	@Override public boolean getRendersChildren(){return true;}
	
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
}
