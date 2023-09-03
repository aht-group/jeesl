package org.jeesl.jsf.components.layout;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.jx.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.components.layout.Slot")
public class Slot extends UIPanel
{
	final static Logger logger = LoggerFactory.getLogger(Slot.class);
	private enum Properties {id,width,styleClass,renderChildren,renderChildrenIfEjb,renderChildrenIfEjbPersisted}
	
	@Override public boolean getRendersChildren(){return true;}
	
	private Boolean renderChilds;
	public Boolean getRenderChilds() {return renderChilds;}
	public void setRenderChilds(Boolean renderChilds) {this.renderChilds = renderChilds;}

	@Override
	public void encodeBegin(FacesContext context) throws IOException
	{
		Map<String,Object> map = super.getAttributes();
		
		ResponseWriter responseWriter = context.getResponseWriter();
		responseWriter.startElement("div", this);
		responseWriter.writeAttribute("id",getClientId(context),"id");
		
		StringBuffer sbStyleClass = new StringBuffer();
		sbStyleClass.append("auGrid_");
		sbStyleClass.append(ComponentAttribute.get(Properties.width,"12",context,this));
		sbStyleClass.append(" jeesl-grid jeesl-grid-");
		sbStyleClass.append(ComponentAttribute.get(Properties.width, "12", context, this));
//		if(map.containsKey(Properties.width.toString()))
//		{
//			sbStyleClass.append(map.get(Properties.width.toString()));
//		}
//		else {sbStyleClass.append(12);}
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