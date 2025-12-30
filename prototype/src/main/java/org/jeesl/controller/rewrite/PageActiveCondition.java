package org.jeesl.controller.rewrite;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageActiveCondition <V extends JeeslSecurityView<?,?,?,?,?,?>> extends HttpCondition implements Serializable,Condition
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(PageActiveCondition.class);
	
	private boolean debugOnInfo; 
	
	private JeeslSecurityBean<?,V,?,?,?,?,?,?> bSecurity;
	
	public PageActiveCondition(boolean debugOnInfo, JeeslSecurityBean<?,V,?,?,?,?,?,?> bSecurity, JeeslIdentity<?,V,?,?,?,?> identity)
	{
		this.debugOnInfo=debugOnInfo;
		this.bSecurity=bSecurity;
	}
		
	@Override public boolean evaluateHttp(HttpServletRewrite event, EvaluationContext context)
    {           	 
		V view = AbstractRewriteProvider.fView(bSecurity,event);
		
		if(Objects.nonNull(view))
		{
			if(debugOnInfo)
		 	{
	    	 	StringBuilder sb = new StringBuilder();
	    	 	sb.append("SecurityView (").append(view.getCode());
	    	 	sb.append(") ").append(event.getAddress().toString());
	    	 	sb.append(" active:").append(view.isVisible());
	    	 	logger.info(sb.toString());
		 	}
			return view.isVisible();
		}
		else
		{
			logger.warn("Assuming active:false, because SecurityView not found for URL:{}",event.getInboundAddress());
			
			return false;
		}
    }
}