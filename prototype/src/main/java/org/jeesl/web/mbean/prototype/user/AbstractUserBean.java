package org.jeesl.web.mbean.prototype.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

import org.jeesl.api.bean.JeeslMenuBean;
import org.jeesl.factory.builder.io.IoLocaleFactoryBuilder;
import org.jeesl.factory.pojo.system.JeeslIdentityFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.context.JeeslSecurityContext;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.page.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.system.security.user.identity.JeeslIdentity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserBean <LOC extends JeeslLocale<?,?,LOC,?>,
											R extends JeeslSecurityRole<?,?,?,V,U,A>,
											V extends JeeslSecurityView<?,?,?,R,U,A>,
											U extends JeeslSecurityUsecase<?,?,?,R,V,A>,
											A extends JeeslSecurityAction<?,?,R,V,U,?>,
											CTX extends JeeslSecurityContext<?,?>,
											USER extends JeeslUser<R>,
											I extends JeeslIdentity<R,V,U,A,CTX,USER>>
				implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractUserBean.class);

	protected JeeslIdentityFactory<I,R,V,U,A,CTX,USER> fId;
	private JeeslMenuBean<V,?,?> bMenu;
	
	protected JeeslLogger jogger;
	
	protected USER user;
	protected I identity;

	protected String ipAddress;
//	protected String sessionId;
	protected String uuid;
	protected String localeCode;
	
	protected boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo=debugOnInfo;}
	protected boolean roleDeveloper; public boolean isRoleDeveloper() {return roleDeveloper;}
	
	protected JeeslConstraint<?,?,?,?,?,?> passwordMismatchConstraint; public JeeslConstraint<?,?,?,?,?,?> getPasswordMismatchConstraint() { return passwordMismatchConstraint; }
	
	protected AbstractUserBean(IoLocaleFactoryBuilder<?,?,?> fbStatus)
	{
//		super(fbStatus.getClassL(),fbStatus.getClassD());
	}

	public void setLocale(String localeCode)
	{
		this.localeCode=localeCode;

		Locale locale = null;
		if(localeCode.equals("de")){locale = Locale.GERMAN;}
		else if(localeCode.equals("en")){locale = Locale.ENGLISH;}
		else if(localeCode.equals("fr")){locale = Locale.FRENCH;}
		else if(localeCode.equals("kin")){locale = new Locale.Builder().setLanguage("rw").setRegion("RW").build();}
		else if(localeCode.equals("id")){locale = new Locale("id", "ID");}
		else if(localeCode.equals("in")){locale = new Locale("id", "ID");}

		logger.debug("localeCode:"+localeCode+" locale:"+locale);
		
		FacesContext ctx = FacesContext.getCurrentInstance();
		if(Objects.nonNull(ctx))
		{
			UIViewRoot root = ctx.getViewRoot();
			if(root!=null) {root.setLocale(locale);}
		}
		localeChanged();
	}
	protected void localeChanged() {}

	public void logout() throws IOException
	{
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.invalidateSession();
	    ec.redirect(ec.getRequestContextPath());
	}

	protected void goToInitalPage()
	{
		ExternalContext eContext = FacesContext.getCurrentInstance().getExternalContext();
	    String forwardedUri = (String) eContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
		try{FacesContext.getCurrentInstance().getExternalContext().redirect(forwardedUri);}
		catch (IOException e) {e.printStackTrace();}
	}
	
	protected void redirectToUrl(String uri)
	{
		try{FacesContext.getCurrentInstance().getExternalContext().redirect(uri);}
		catch (IOException e) {e.printStackTrace();}
	}
}