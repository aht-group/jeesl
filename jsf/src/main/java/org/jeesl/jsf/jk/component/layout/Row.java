package org.jeesl.jsf.jk.component.layout;

import java.io.IOException;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIPanel;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.jk.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.layout.Row")
public class Row extends UIPanel
{	
	final static Logger logger = LoggerFactory.getLogger(Row.class);
	
	private static enum Properties {width,renderChildren,renderChildrenIfEjb,renderChildrenIfEjbPersisted,dir}
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");

		StringBuilder sbStyleClass = new StringBuilder();
		sbStyleClass.append("jeesl-row jeesl-row-");
		sbStyleClass.append(ComponentAttribute.get(Properties.width, "12", context, this));

		if(ComponentAttribute.available(Properties.dir,context,this))
		{
			logger.info(Properties.dir+": "+ComponentAttribute.get(Properties.dir,"ltr",context,this));
			sbStyleClass.append(" jeesl-row-");
			sbStyleClass.append(ComponentAttribute.get(Properties.dir, "ltr", context, this));
		}
		
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