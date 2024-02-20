package org.jeesl.controller.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.user.JeeslSecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslLocaleWebController <L extends JeeslLang, D extends JeeslDescription,
												LOC extends JeeslLocale<L,D,LOC,?>
//,USER extends JeeslSecurityUser
												>
						extends AbstractJeeslWebController
						implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslLocaleWebController.class);
	
	protected JeeslLocaleProvider<LOC> lp;
	
	private final List<LOC> locales; public List<LOC> getLocales() {return locales;}
	
	protected final EjbLangFactory<L> efLang;
	protected final EjbDescriptionFactory<D> efDescription;
	
	protected Class<L> cL;
	protected Class<D> cD;

	public AbstractJeeslLocaleWebController(final Class<L> cL, final Class<D> cD)
	{
		super();
		this.cL = cL;
		this.cD = cD;
		
		locales = new ArrayList<LOC>();
		
		efLang = EjbLangFactory.instance(cL);
		efDescription = EjbDescriptionFactory.instance(cD);
		
		debugOnInfo = false;
	}
	
	protected void postConstructLocaleWebController(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage)
	{
		super.postConstructWebController(bMessage);
		this.lp=lp;
		
		locales.addAll(lp.getLocales());
	}
}