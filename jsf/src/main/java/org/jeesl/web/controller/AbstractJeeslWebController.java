package org.jeesl.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslLocaleProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslWebController <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>> implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslWebController.class);
	
	protected JeeslFacesMessageBean bMessage;
	protected JeeslLocaleProvider<LOC> lp;
	protected JeeslLogger jogger;
	
	private final List<LOC> locales; public List<LOC> getLocales() {return locales;}
	
	protected final EjbLangFactory<L> efLang;
	protected final EjbDescriptionFactory<D> efDescription;
	
	protected boolean debugOnInfo;
	public void activateDebuggingOnInfo(JeeslLogger jogger)
	{
		this.debugOnInfo=true;
		this.jogger=jogger;
	}
	
	protected Class<L> cL;
	protected Class<D> cD;

//	protected NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;} public void setNnb(NullNumberBinder nnb) {this.nnb = nnb;}

	public AbstractJeeslWebController(final Class<L> cL, final Class<D> cD)
	{
		this.cL = cL;
		this.cD = cD;
		
		locales = new ArrayList<LOC>();
		
		efLang = EjbLangFactory.instance(cL);
		efDescription = new EjbDescriptionFactory<D>(cD);
		
		debugOnInfo = false;
	}
	
	protected void postConstructWebController(JeeslLocaleProvider<LOC> lp, JeeslFacesMessageBean bMessage
			)
	{
		this.lp=lp;
		this.bMessage=bMessage;
		
		locales.addAll(lp.getLocales());
	}
}