package org.jeesl.controller.rewrite;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRewriteProvider <V extends JeeslSecurityView<?,?,?,?,?,?>>
		extends HttpConfigurationProvider implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractRewriteProvider.class);

	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	private JeeslSecurityBean<?,?,V,?,?,?,?,?,?> bSecurity;

	protected String forwardDeactivated;
	protected String forwardLogin;
	protected String forwardDenied;

	public AbstractRewriteProvider(SecurityFactoryBuilder<?,?,?,?,V,?,?,?,?,?,?,?,?,?,?,?> fbSecurity)
	{
		debugOnInfo = false;
		forwardDeactivated = "/jsf/settings/system/security/page/deactivated.xhtml";
		forwardLogin = "/jsf/settings/system/security/page/unauthorized.xhtml";
		forwardDenied = "/jsf/settings/system/security/page/denied.xhtml";
	}

	public void postConstruct(JeeslSecurityBean<?,?,V,?,?,?,?,?,?> bSecurity)
	{
		this.bSecurity=bSecurity;
	}

	protected ConfigurationBuilder build(Condition pageActive, Condition notLoggedIn, Condition pageDenied)
	{
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
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
				cb = cb.addRule(Join.path(view.getViewPattern()).to(forwardDeactivated).withInboundCorrection()).when(Direction.isInbound().andNot(pageActive));
				cb = cb.addRule(Join.path(view.getUrlMapping()).to(forwardDeactivated).withInboundCorrection()).when(Direction.isInbound().andNot(pageActive));
				cb = cb.addRule(Join.path(view.getViewPattern()).to(forwardLogin).withInboundCorrection()).when(Direction.isInbound().and(notLoggedIn));
				cb = cb.addRule(Join.path(view.getUrlMapping()).to(forwardLogin).withInboundCorrection()).when(Direction.isInbound().and(notLoggedIn));
				cb = cb.addRule(Join.path(view.getViewPattern()).to(forwardDenied).withInboundCorrection()).when(Direction.isInbound().and(pageDenied));
				cb = cb.addRule(Join.path(view.getUrlMapping()).to(forwardDenied).withInboundCorrection()).when(Direction.isInbound().and(pageDenied));
	
				//Flip
				cb = cb.addRule(Join.path(view.getUrlMapping()).to(view.getViewPattern()).withInboundCorrection()).when(Direction.isInbound().and(pageActive));
//				cb = cb.addRule(Join.path(view.getUrlMapping()).to(view.getViewPattern()).withInboundCorrection()).when(Direction.isInbound());
			}
			else
			{
				logger.warn("No Rule will be created for "+view.toString());
			}
		}
		logger.info("Rules created for "+views.size()+" Views in "+ptt.toTotalTime());
		return cb;
	}

	public static String getUrlMapping(String context, String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			int indexStart = url.getPath().indexOf(context);
			int indexEnd = url.getPath().length();
			
			String mapping = url.getPath().substring(indexStart+context.length(), indexEnd);
//			logger.info("Context: "+context+" URL:"+urlString+" Mapping:"+mapping);
			
			return mapping;
		}
		catch (MalformedURLException e) {e.printStackTrace();}
		return null;
	}
}