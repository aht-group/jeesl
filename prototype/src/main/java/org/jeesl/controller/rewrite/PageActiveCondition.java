package org.jeesl.controller.rewrite;

import java.io.Serializable;
import java.util.Objects;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityCategory;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityMenu;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityTemplate;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageActiveCondition <C extends JeeslSecurityCategory<?,?>,
											R extends JeeslSecurityRole<?,?,C,V,U,A,USER>,
											V extends JeeslSecurityView<?,?,C,R,U,A>,
											U extends JeeslSecurityUsecase<?,?,C,R,V,A>,
											A extends JeeslSecurityAction<?,?,R,V,U,AT>,
											AT extends JeeslSecurityTemplate<?,?,C>,
											CTX extends JeeslSecurityContext<?,?>,
											M extends JeeslSecurityMenu<?,V,CTX,M>,
											USER extends JeeslUser<R>>
		extends HttpCondition
		implements Condition,Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(PageActiveCondition.class);
	
	private boolean debugOnInfo; 
	
	private JeeslSecurityBean<?,?,C,R,V,U,A,AT,?,CTX,M,USER> bSecurity;
	
	public PageActiveCondition(boolean debugOnInfo, JeeslSecurityBean<?,?,C,R,V,U,A,AT,?,CTX,M,USER> bSecurity, JeeslIdentity<R,V,U,A,CTX,USER> identity)
	{
		this.debugOnInfo=debugOnInfo;
		this.bSecurity=bSecurity;
	}
	
	@Override public boolean evaluateHttp(HttpServletRewrite event, EvaluationContext context)
    {           	 
		String url = AbstractRewriteProvider.getUrlMapping(event.getContextPath(), event.getAddress().toString());
		V view = bSecurity.findViewByUrlMapping(url);
		if(Objects.isNull(view)) {view = bSecurity.findViewByHttpPattern(url);}
		
		if(debugOnInfo)
	 	{
    	 	logger.info(event.getContextPath());
    	 	logger.info(event.getAddress().toString());
    	 	logger.info(event.getInboundAddress().toString());
    	 	logger.info("pageActive: "+url);
    		if(Objects.nonNull(view)) {logger.info("View: "+view.getCode()+" "+view.isVisible());}
    		else {logger.warn("View not found");}
	 	}
		
		if(Objects.nonNull(view))
		{
			return view.isVisible();
		}
		else
		{
			logger.warn("Assuming active=false, because view not found for url: "+url);
			return false;
		}
    }
}