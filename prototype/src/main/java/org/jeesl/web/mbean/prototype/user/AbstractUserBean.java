package org.jeesl.web.mbean.prototype.user;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;

import org.jeesl.api.bean.JeeslMenuBean;
import org.jeesl.api.facade.core.JeeslUserFacade;
import org.jeesl.api.facade.system.JeeslSecurityFacade;
import org.jeesl.factory.builder.system.LocaleFactoryBuilder;
import org.jeesl.factory.pojo.system.JeeslIdentityFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityAction;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityUsecase;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityView;
import org.jeesl.interfaces.model.system.security.user.JeeslIdentity;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											R extends JeeslSecurityRole<L,D,?,V,U,A,USER>,
											V extends JeeslSecurityView<L,D,?,R,U,A>,
											U extends JeeslSecurityUsecase<L,D,?,R,V,A>,
											A extends JeeslSecurityAction<L,D,R,V,U,?>,
											USER extends JeeslUser<R>,
											I extends JeeslIdentity<R,V,U,A,USER>>
				extends AbstractAdminBean<L,D,LOC>
				implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractUserBean.class);

	private JeeslUserFacade<USER> fUser;
	protected JeeslSecurityFacade<L,D,?,R,V,U,A,?,?,?,USER> fSecurity;
	protected JeeslIdentityFactory<I,R,V,U,A,USER> fId;
	private JeeslMenuBean<V,?,?> bMenu;

	protected JeeslLogger jogger;
	
	protected USER user;
	protected I identity;

	protected String ipAddress;
//	protected String sessionId;
	protected String uuid;
	protected String localeCode;

	protected AbstractUserBean(LocaleFactoryBuilder<L,D,?> fbStatus)
	{
		super(fbStatus.getClassL(),fbStatus.getClassD());
	}

	protected void postConstruct(JeeslUserFacade<USER> fUser, JeeslSecurityFacade<L,D,?,R,V,U,A,?,?,?,USER> fSecurity)
	{
		this.fUser=fUser;
		this.fSecurity=fSecurity;
	}

	protected void postConstruct(JeeslUserFacade<USER> fUser, JeeslSecurityFacade<L,D,?,R,V,U,A,?,?,?,USER> fSecurity, JeeslMenuBean<V,?,?> bMenu)
	{
		this.fUser=fUser;
		this.fSecurity=fSecurity;
		this.bMenu=bMenu;
	}

	public void setLocale(String localeCode)
	{
		this.localeCode=localeCode;

		Locale locale = null;
		if(localeCode.equals("de")){locale = Locale.GERMAN;}
		else if(localeCode.equals("en")){locale = Locale.ENGLISH;}
		else if(localeCode.equals("fr")){locale = Locale.FRENCH;}
		else if(localeCode.equals("kin")){locale = new Locale.Builder().setLanguage("rw").setRegion("RW").build();}

		logger.info("localeCode:"+localeCode+" locale"+locale);
		
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
		if(bMenu!=null) {bMenu.updateLocale(localeCode);}
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
}