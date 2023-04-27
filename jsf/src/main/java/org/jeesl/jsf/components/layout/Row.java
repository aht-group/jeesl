package org.jeesl.jsf.components.layout;

import java.io.IOException;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.util.ComponentAttribute;

@FacesComponent("org.jeesl.jsf.components.layout.Row")
public class Row extends UIPanel
{	
	private static enum Properties {width,renderChildren,renderChildrenIfEjb,renderChildrenIfEjbPersisted}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");

		StringBuffer sbStyleClass = new StringBuffer();
		sbStyleClass.append("jeesl-row jeesl-row-");
		sbStyleClass.append(ComponentAttribute.get(Properties.width, "12", context, this));

		responseWriter.writeAttribute("class", sbStyleClass, null);
	}

	@Override
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.endElement("div");
	}
	
	@Override public boolean getRendersChildren(){return true;}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
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
			for(UIComponent uic : this.getChildren())
			{
				uic.encodeAll(context);
			}
		}
	}
}