package org.jeesl.controller.rewrite;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.ocpsoft.rewrite.config.CompositeRule;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.config.Rule;
import org.ocpsoft.rewrite.config.RuleBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRewriteProvider <V extends JeeslSecurityView<?,?,?,?,?,?>>
												extends HttpConfigurationProvider implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractRewriteProvider.class);

	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	protected String forwardUnauthorized;
	protected String forwardDeactivated;
	protected String forwardDenied;

	public AbstractRewriteProvider(SecurityFactoryBuilder<?,?,?,?,V,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbSecurity)
	{
		debugOnInfo = false;
		forwardUnauthorized = "/jsf/settings/system/security/redirect/unauthorized.xhtml";
		forwardDeactivated = "/jsf/settings/system/security/page/deactivated.xhtml";
		forwardDenied = "/jsf/settings/system/security/page/denied.xhtml";
	}

	protected ConfigurationBuilder build(JeeslSecurityBean<?,V,?,?,?,?,?,?> bSecurity, Condition pageActive, Condition notLoggedIn, Condition pageDenied)
	{
		ConfigurationBuilder cb = ConfigurationBuilder.begin();	
		cb = cb.addRule(Join.path("/").to("/index.jsf"));
		
		// Activate all of these
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(Path.matches("/javax.faces.resource/showcase.css.jsf")));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(Path.matches("/javax.faces.resource/core.js.jsf")));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(Path.matches("/javax.faces.resource/12/sb/roles.svg.jsf")));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(Path.matches("/javax.faces.resource/12/sb/shield-green.svg.jsf")));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().andNot(pageActive)).perform(Forward.to(forwardDeactivated));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(notLoggedIn)).perform(Forward.to(forwardLogin));
//		cb = (ConfigurationBuilder)cb.addRule().when(Direction.isInbound().and(pageDenied)).perform(Forward.to(forwardDenied));

		List<V> views = bSecurity.getViews();
		for(V view : views)
		{
			logger.debug("Building Rule for "+view.toString());
			if(ObjectUtils.allNotNull(view.getViewPattern(),view.getUrlMapping()) && view.getViewPattern().contains("/") && view.getUrlMapping().contains("/"))
			{
				//Deactivate 6
				cb.addRule(Join.path(view.getViewPattern()).to(forwardDeactivated)).when(Direction.isInbound().andNot(pageActive));
				cb.addRule(Join.path(view.getUrlMapping()).to(forwardDeactivated)).when(Direction.isInbound().andNot(pageActive));
				
				cb.addRule(Join.path(view.getViewPattern()).to(forwardUnauthorized)).when(Direction.isInbound().and(notLoggedIn));
				cb.addRule(Join.path(view.getUrlMapping()).to(forwardUnauthorized)).when(Direction.isInbound().and(notLoggedIn));
				
				cb.addRule(Join.path(view.getViewPattern()).to(forwardDenied)).when(Direction.isInbound().and(pageDenied));
				cb.addRule(Join.path(view.getUrlMapping()).to(forwardDenied)).when(Direction.isInbound().and(pageDenied));
	
				//Flip
				cb.addRule(Join.path(view.getUrlMapping()).to(view.getViewPattern())).when(Direction.isInbound().and(pageActive));
//				cb = cb.addRule(Join.path(view.getUrlMapping()).to(view.getViewPattern())).when(Direction.isInbound());
			}
			else
			{
				logger.warn("No Rule will be created for "+view.toString());
			}
		}
		logger.info("Rules created for {} Views",views.size());
		return cb;
	}
	
	public static <V extends JeeslSecurityView<?,?,?,?,?,?>> V fView(JeeslSecurityBean<?,V,?,?,?,?,?,?> bSecurity, HttpServletRewrite event)
	{
		String url = AbstractRewriteProvider.getUrlMapping(event.getContextPath(), event.getAddress().toString());
		
		V view = AbstractRewriteProvider.viewByUrl(bSecurity,url);
		
		if(Objects.isNull(view))
		{
			String urlPara = AbstractRewriteProvider.getParamer(event);
			logger.debug("View for {} not found, trying to find parametrized URL",url,urlPara);
			view = AbstractRewriteProvider.viewByUrl(bSecurity,urlPara);
		}
		return view;
	}
	
	private static <V extends JeeslSecurityView<?,?,?,?,?,?>> V viewByUrl(JeeslSecurityBean<?,V,?,?,?,?,?,?> bSecurity, String url)
	{
		V view = bSecurity.findViewByUrlMapping(url);
		if(Objects.isNull(view)) {view = bSecurity.findViewByHttpPattern(url);}
		return view;
	}

	public static String getUrlMapping(String context, String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			int indexStart = url.getPath().indexOf(context);
			int indexEnd = url.getPath().length();
			
			String mapping = url.getPath().substring(indexStart+context.length(), indexEnd);
			
			return mapping;
		}
		catch (MalformedURLException e) {e.printStackTrace();}
		return null;
	}
	
	public static String getParamer(HttpServletRewrite event)
	{
		String url = null;;
		for(Rule r : event.getEvaluatedRules())
		{
			url = AbstractRewriteProvider.extractPatternFromRuleString(r);
			if(Objects.nonNull(url)) {return url;}
		}
		return url;
	}
	
	private static String extractPatternFromRuleString(Rule rule)
	{
	    String s = rule.toString();
	    // Suche nach Join.path("...")
	    int start = s.indexOf("Join.path(\"");
	    if (start < 0) return null;
	    start += "Join.path(\"".length();
	    int end = s.indexOf("\")", start);
	    if (end < 0) return null;

	    return s.substring(start, end); // -> /me/meeting/{param}
	}
}