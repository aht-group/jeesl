package org.jeesl.jsf.handler;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.controller.handler.system.locales.JeeslTranslationProvider;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.jsf.jk.util.FacesContextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMessageHandler <L extends JeeslLang, D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>>
//					implements JeeslFacesMessageBean
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(JeeslMessageHandler.class);

	private JeeslTranslationBean<L,D,LOC> bTranslation;
	private JeeslTranslationProvider<LOC> tp;
	protected String localeCode;
	
	protected final JeeslFacesContextMessage fcm;
	
	public JeeslMessageHandler(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	public void postConstruct(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation)
	{
		this.localeCode=localeCode;
		this.bTranslation=bTranslation;
	}
	public void postConstruct(String localeCode, JeeslTranslationProvider<LOC> tp)
	{
		this.localeCode=localeCode;
		this.tp=tp;
	}
	
//	@Override
	public <T extends EjbWithId> void growlSuccessSaved(T t)
	{
		String summary = null;
		String detail= null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.toLabel(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summarySuccess);
			detail = tp.toDescription(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Detail.detailSaved);
		}
		else if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"jeeslFmSuccess");
			detail = bTranslation.get(localeCode,"jeeslFmObjectSaved");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "** SUCCESS **";}
		if(ObjectUtils.isEmpty(detail)) {detail = "** Object Saved **";}
		
//		logger.info("growlSuccessSaved "+t.toString());
		fcm.info(JeeslFacesContextMessage.Faces.growl,summary,detail);
	}
}