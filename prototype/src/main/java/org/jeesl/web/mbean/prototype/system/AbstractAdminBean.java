package org.jeesl.web.mbean.prototype.system;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.factory.ejb.system.status.EjbDescriptionFactory;
import org.jeesl.factory.ejb.system.status.EjbLangFactory;
import org.jeesl.interfaces.controller.handler.system.io.JeeslLogger;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.StringUtil;

public class AbstractAdminBean <L extends JeeslLang, D extends JeeslDescription
, LOC extends JeeslLocale<L,D,LOC,?>
>implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractAdminBean.class);
	
	protected Class<L> cL;
	protected Class<D> cD;
	
	protected JeeslFacesMessageBean bMessage;
	protected JeeslTranslationBean<L,D,?> bTranslation;
	protected JeeslLogger jogger;
	
	protected boolean debugOnInfo; protected void setDebugOnInfo(boolean debugOnInfo){this.debugOnInfo=debugOnInfo;}
	protected String[] langs; public String[] getLangs() {return langs;}
	protected String[] localeCodes; public String[] getLocaleCodes() {return localeCodes;}
	private final List<LOC> locales; public List<LOC> getLocales() {return locales;}
	
	protected EjbLangFactory<L> efLang;
	protected EjbDescriptionFactory<D> efDescription;
	
	protected boolean hasDeveloperAction; public boolean isHasDeveloperAction() {return hasDeveloperAction;}
	protected boolean hasAdministratorAction; public boolean isHasAdministratorAction() {return hasAdministratorAction;}
	protected boolean hasTranslatorAction;
	
	protected NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;} public void setNnb(NullNumberBinder nnb) {this.nnb = nnb;}

	
	public AbstractAdminBean(final Class<L> cL, final Class<D> cD)
	{
		this.cL = cL;
		this.cD = cD;
		
		locales = new ArrayList<LOC>();
		
		efLang = EjbLangFactory.instance(cL);
		efDescription = new EjbDescriptionFactory<D>(cD);
		
		debugOnInfo = false;
		
		hasDeveloperAction = true;
		hasAdministratorAction = true;
		hasTranslatorAction = true;
		
		uiAllowAdd = true;
		uiAllowSave = true;
		uiAllowRemove = true;
		uiAllowReorder = true;
		uiAllowCode = true;
		
		uiShowInvisible = true;
		uiShowDocumentation = true;
		
		nnb = new NullNumberBinder();
	}
	
	protected void initJeeslAdmin(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage)
	{
		this.bTranslation=bTranslation;
		this.bMessage=bMessage;
		
		locales.addAll(bTranslation.getLocales());
		
		logger.info("bTranslation!=null: "+(bTranslation!=null));
		logger.info("bTranslation.getLangKeys()!=null: "+(bTranslation.getLangKeys()!=null));
		
		localeCodes=bTranslation.getLangKeys().toArray(new String[bTranslation.getLangKeys().size()]);
		langs=localeCodes;
	}
	
	@Deprecated //Use initJeeslAdmin instead
	protected void initAdmin(String[] langs, final Class<L> cL, final Class<D> cD, JeeslFacesMessageBean bMessage)
	{
		this.localeCodes=langs;
		this.langs=langs;
		this.bMessage=bMessage;
		
		efLang = EjbLangFactory.instance(cL);
		efDescription = new EjbDescriptionFactory<D>(cD);
	}
	
	//Security Handling
	protected boolean uiAllowAdd; public boolean isUiAllowAdd() {return uiAllowAdd;}
	protected boolean uiAllowSave; public boolean getUiAllowSave() {return uiAllowSave;}
	protected boolean uiAllowRemove; public boolean isUiAllowRemove() {return uiAllowRemove;}
	protected boolean uiAllowReorder; public boolean getUiAllowReorder() {return uiAllowReorder;}
	protected boolean uiAllowCode;public boolean isUiAllowCode() {return uiAllowCode;}
	protected boolean uiShowInvisible; public boolean isUiShowInvisible() {return uiShowInvisible;}
	protected boolean uiShowDocumentation; public boolean isUiShowDocumentation() {return uiShowDocumentation;}
	
	@SuppressWarnings("rawtypes")
	protected void updateSecurity2(JeeslJsfSecurityHandler jsfSecurityHandler, String viewCode)
	{
		String actionDeveloper = viewCode+".Developer";
		String actionAdministrator = viewCode+".Administrator";
		String actionTranslator = viewCode+".Translator";
		
		hasDeveloperAction = jsfSecurityHandler.allow(actionDeveloper);
		hasAdministratorAction = jsfSecurityHandler.allow(actionAdministrator);
		hasTranslatorAction = jsfSecurityHandler.allow(actionTranslator);
		
		uiAllowAdd = hasDeveloperAction || hasAdministratorAction;
		uiAllowSave = hasDeveloperAction || hasAdministratorAction || hasTranslatorAction;
		uiAllowRemove = hasDeveloperAction || hasAdministratorAction;
		uiAllowReorder = hasDeveloperAction || hasAdministratorAction;
		uiAllowCode = hasDeveloperAction;
		
		uiShowInvisible = hasDeveloperAction;
		uiShowDocumentation = hasDeveloperAction;
	}
	
	protected void debugUi(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("UI Settings");
			logger.info("\t"+uiAllowAdd+" allowStatusAdd");
			logger.info("\t"+uiAllowReorder+" uiAllowReorder");
			logger.info("\t"+uiAllowCode+" uiAllowCode");
			logger.info("\t"+uiAllowSave+" uiAllowSave");
			logger.info("\t"+uiAllowRemove+" uiAllowRemove");
		}
	}
	
	protected void debugSecurity(boolean debug)
	{
		if(debug)
		{
			logger.info(StringUtil.stars());
			logger.info("Security Settings");
			logger.info("\t"+hasDeveloperAction+" hasDeveloperAction");
			logger.info("\t"+hasAdministratorAction+" hasAdministratorAction");
			logger.info("\t"+hasTranslatorAction+" hasTranslatorAction");
		}
	}
}