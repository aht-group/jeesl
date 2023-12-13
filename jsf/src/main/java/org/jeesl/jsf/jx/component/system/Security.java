package org.jeesl.jsf.jx.component.system;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.interfaces.web.JeeslJsfWorkflowHandler;
import org.jeesl.jsf.jx.util.ComponentAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FacesComponent("org.jeesl.jsf.component.Security")
public class Security extends UIPanel
{
	final static Logger logger = LoggerFactory.getLogger(Security.class);
	private boolean debugOnInfo = false;
	
	private static enum Properties {action,actionSuffix,handler,allow,workflow}
	private static enum Facets {denied}
	
	@Override public boolean getRendersChildren(){return true;}
	
	private Boolean renderChilds; public Boolean getRenderChilds() {return renderChilds;} public void setRenderChilds(Boolean renderChilds) {this.renderChilds = renderChilds;}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException
	{
		boolean accessGranted = false;
		boolean workflowAllow = true;
		
		if(ComponentAttribute.available(Properties.workflow,context,this))
		{
			JeeslJsfWorkflowHandler wfh = (JeeslJsfWorkflowHandler)ComponentAttribute.getObject(Properties.workflow,null,context,this);
			workflowAllow = wfh.isAllowEntityModifications() || wfh.isAllowAdminModifications();
			if(debugOnInfo) {logger.info(JeeslJsfWorkflowHandler.class.getSimpleName()+" evaluated workflowAllow:"+workflowAllow);}
		}
		
		boolean accessGrantedAttribute = ComponentAttribute.getBoolean(Properties.allow,true,context,this);
		try
		{
			ValueExpression ve = this.getValueExpression(Properties.handler.toString());
			if(ve==null){throw new JeeslNotFoundException("");}
			JeeslJsfSecurityHandler<?,?,?,?,?,?> handler = (JeeslJsfSecurityHandler<?,?,?,?,?,?>)ve.getValue(context.getELContext());
			
			String action;
			String actionSuffix = ComponentAttribute.get(Properties.actionSuffix,null,context,this);
			if(actionSuffix!=null)
			{
				action = handler.getPageCode()+"."+actionSuffix;
				if(debugOnInfo) {logger.info("SUFFIX:"+action);}
			}
			else
			{
				action = ComponentAttribute.get(Properties.action.toString(),context,this);
			}
			
			accessGranted = (handler.allow(action) && accessGrantedAttribute);
			if(debugOnInfo) {logger.info(JeeslJsfSecurityHandler.class.getSimpleName()+" evaluated accessGranted:"+accessGranted);}
		}
		catch (JeeslNotFoundException e)
		{
			accessGranted = accessGrantedAttribute;
			if(debugOnInfo) {logger.info("No "+JeeslJsfSecurityHandler.class.getSimpleName()+", so accessGranted:"+accessGranted);}
		}
		
		if(debugOnInfo) {logger.info("Final: accessGranted:"+accessGranted+" workflowAllow:"+workflowAllow);}
			
		if(accessGranted && workflowAllow)
		{
			for(UIComponent uic : this.getChildren())
			{
				uic.encodeAll(context);
			}
		}
		else if(this.getFacets().containsKey(Facets.denied.toString()))
		{
			this.getFacet(Facets.denied.toString()).encodeAll(context);
		}
	}
}