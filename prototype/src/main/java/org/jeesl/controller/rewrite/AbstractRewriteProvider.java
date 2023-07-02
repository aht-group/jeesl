package org.jeesl.controller.rewrite;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.jeesl.api.bean.JeeslSecurityBean;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.ocpsoft.rewrite.config.Condition;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractRewriteProvider <V extends JeeslSecurityView<?,?,?,?,U,?>,
											U extends JeeslSecurityUsecase<?,?,?,?,V,?>>
		extends HttpConfigurationProvider
		implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractRewriteProvider.class);

	protected boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}

	private JeeslSecurityBean<?,?,?,?,V,U,?,?,?,?,?,?> bSecurity;

	private U usecase; public U getUsecase(){return usecase;} public void setUsecase(U usecase){this.usecase = usecase;}

	protected String forwardDeactivated;
	protected String forwardLogin;
	protected String forwardDenied;

	public AbstractRewriteProvider(SecurityFactoryBuilder<?,?,?,?,V,U,?,?,?,?,?,?,?,?,?,?> fbSecurity)
	{
		debugOnInfo = false;
		forwardDeactivated = "/jsf/settings/system/security/page/deactivated.xhtml";
		forwardLogin = "/jsf/settings/system/security/page/unauthorized.xhtml";
		forwardDenied = "/jsf/settings/system/security/page/denied.xhtml";
	}

	public void postConstruct(JeeslSecurityBean<?,?,?,?,V,U,?,?,?,?,?,?> bSecurity)
	{
		this.bSecurity=bSecurity;
	}

	protected ConfigurationBuilder build(Condition pageActive, Condition notLoggedIn, Condition pageDenied)
	{
		ConfigurationBuilder config = ConfigurationBuilder.begin();
		config = config.addRule(Join.path("/").to("index.jsf"));
		List<V> views = bSecurity.getViews();
		for(V view : views)
		{
			logger.debug("Building Rule for "+view.toString());
			if(view.getViewPattern() !=null && view.getUrlMapping() !=null && view.getViewPattern().contains("/") && view.getUrlMapping().contains("/"))
			{
				config = config.addRule(Join.path(view.getViewPattern()).to(forwardDeactivated)).when(Direction.isInbound().andNot(pageActive));
				config = config.addRule(Join.path(view.getUrlMapping()).to(forwardDeactivated)).when(Direction.isInbound().andNot(pageActive));
	
				config = config.addRule(Join.path(view.getViewPattern()).to(forwardLogin)).when(Direction.isInbound().and(notLoggedIn));
				config = config.addRule(Join.path(view.getUrlMapping()).to(forwardLogin)).when(Direction.isInbound().and(notLoggedIn));
	
				config = config.addRule(Join.path(view.getViewPattern()).to(forwardDenied)).when(Direction.isInbound().and(pageDenied));
				config = config.addRule(Join.path(view.getUrlMapping()).to(forwardDenied)).when(Direction.isInbound().and(pageDenied));
	
				config = config.addRule(Join.path(view.getUrlMapping()).to(view.getViewPattern())).when(Direction.isInbound().and(pageActive));
			}
		}
		logger.info("Rules created for "+views.size()+" Views");
		return config;
	}

	public static String getUrlMapping(String context, String urlString)
	{
		try
		{
			URL url = new URL(urlString);
			int indexStart = url.getPath().indexOf(context);
			int indexEnd = url.getPath().length();
			return url.getPath().substring(indexStart+context.length(), indexEnd);
		}
		catch (MalformedURLException e) {e.printStackTrace();}
		return urlString;
	}
}