package org.jeesl.jsf.components.layout;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sf.ahtutils.jsf.util.ComponentAttribute;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.primefaces.component.outputlabel.OutputLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.layout.InputGrid")
public class InputGrid extends UIPanel
{
	final static Logger logger = LoggerFactory.getLogger(InputGrid.class);
	private static enum Properties {id,inputFields,labelWidth,styleClass,renderChildren,renderChildrenIfEjb,renderChildrenIfEjbPersisted}
	
	@Override public boolean getRendersChildren(){return true;}
	
	private Boolean renderChilds;
	public Boolean getRenderChilds() {return renderChilds;}
	public void setRenderChilds(Boolean renderChilds) {this.renderChilds = renderChilds;}
	
	private AtomicInteger counter = new AtomicInteger(0);
	private int columnCount = 2;
	private int labelWidth = 3;
	
	private int classifyChildGroup()
	{
		final int i = counter.getAndIncrement();
		int remainder = i % columnCount;
		return (remainder == 0) ? i : i - remainder;
	}

	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		Map<String,Object> map = getAttributes();
		
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");
		
		StringBuffer sbStyleClass = new StringBuffer();
		sbStyleClass.append("ui-fluid input-grid");
		
		if(map.containsKey(Properties.styleClass.toString()))
		{
			sbStyleClass.append(" ").append(map.get(Properties.styleClass.toString()));
		}
		responseWriter.writeAttribute("class",sbStyleClass.toString(),null);
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
		try
		{
			columnCount = Integer.parseInt(ComponentAttribute.get(Properties.inputFields, "1", context, this)) + 1;
		}
		catch(NumberFormatException e) {}
		
		try
		{
			labelWidth = Integer.parseInt(ComponentAttribute.get(Properties.labelWidth, "3", context, this));
		}
		catch(NumberFormatException e) {}
		
		boolean rChildren = ComponentAttribute.getBoolean(Properties.renderChildren, true, context, this);
		
		if(rChildren && ComponentAttribute.available(Properties.renderChildrenIfEjb,context,this))
		{
			EjbWithId ejb = ComponentAttribute.getObject(EjbWithId.class,Properties.renderChildrenIfEjb.toString(),context,this);
			if(ejb==null){rChildren=false;}
		}
		
		if(rChildren && ComponentAttribute.available(Properties.renderChildrenIfEjbPersisted,context,this))
		{
			EjbWithId ejb = ComponentAttribute.getObject(EjbWithId.class,Properties.renderChildrenIfEjbPersisted.toString(),context,this);
			if(ejb==null){rChildren=false;}
			else if(ejb.getId()==0){rChildren=false;}
		}
		
		if(rChildren)
		{
			counter = new AtomicInteger(0);
			List<List<UIComponent>> childGroup = this.getChildren().stream().collect(Collectors.groupingBy(child -> classifyChildGroup())).values().stream().collect(Collectors.toList());
			
			float inputWidth = (12 - labelWidth) / (columnCount - 1);
			
			for (List<UIComponent> group : childGroup)
			{
				UIPanel groupChild = new UIPanel();
				ResponseWriter responseWriter = context.getResponseWriter();
				responseWriter.startElement("div", groupChild);
				responseWriter.writeAttribute("class","p-field p-grid",null);
				
				for (UIComponent child : group)
				{
					if (child instanceof OutputLabel)
					{
						Map<String, Object> attributes = child.getAttributes();
						StringBuffer styleClass = new StringBuffer("p-col p-md-" + labelWidth);
						if (attributes.containsKey("styleClass")) {
							styleClass.append(" " + (String)attributes.get("styleClass"));
						}
						attributes.put("styleClass", styleClass.toString());
						
						child.encodeAll(context);
					}
					else
					{
						
        				UIPanel inputChild = new UIPanel();
        				responseWriter.startElement("div", inputChild);
        				responseWriter.writeAttribute("class", "p-col p-md-" + (int)inputWidth, null);
						
        				child.encodeAll(context);
				
        				responseWriter.endElement("div");
					}
				}
				
				responseWriter.endElement("div");
			}
			
//			for(UIComponent uic : this.getChildren())
//			{
//				uic.encodeAll(context);
//			}
		}
	}
}