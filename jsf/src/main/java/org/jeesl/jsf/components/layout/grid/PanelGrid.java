package org.jeesl.jsf.components.layout.grid;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.commons.collections4.ListUtils;
import org.jeesl.jsf.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.layout.grid.PanelGrid")
public class PanelGrid extends UINamingContainer
{
	final static Logger logger = LoggerFactory.getLogger(PanelGrid.class);
	private static enum Properties {id,columns,styleClass}
	
	@Override public boolean getRendersChildren() {return true;}

	@Override public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");
		
		StringBuffer sbStyleClass = new StringBuffer();
		sbStyleClass.append("ui-fluid jeesl-input-grid ");
		sbStyleClass.append(ComponentAttribute.get(Properties.styleClass, "", context, this));
		responseWriter.writeAttribute("class",sbStyleClass.toString(),null);
	}

	@Override public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("div");
	}
	
	@Override public void encodeChildren(FacesContext context) throws IOException
	{
		int columns = ComponentAttribute.getInteger(Properties.columns.toString(), 2, context, this);
		
		List<UIComponent> children = this.getChildren().stream().filter(c -> c.isRendered()).collect(Collectors.toList());
		List<List<UIComponent>> groups = ListUtils.partition(children,columns);
		
		ResponseWriter responseWriter = context.getResponseWriter();
		for (List<UIComponent> group : groups)
		{
			responseWriter.startElement("div", this);
			responseWriter.writeAttribute("class","p-field p-grid",null);
			
			for(int i=0;i<group.size();i++)
			{
				UIComponent child = group.get(i);
				responseWriter.startElement("div", this);
				
				StringBuilder sb = new StringBuilder();
				sb.append("p-col");
				if(i==0) {sb.append(" p-md-").append(3);}
				else {sb.append(" p-col-count-").append(i);}
				
				responseWriter.writeAttribute("class", sb.toString(), null);
				
				child.encodeAll(context);
		
				responseWriter.endElement("div");
			}		
			responseWriter.endElement("div");
		}
	}
}