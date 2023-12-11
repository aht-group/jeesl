package org.jeesl.jsf.handler;

import java.util.Objects;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMessageHandler <L extends JeeslLang, D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>>
//					implements JeeslFacesMessageBean
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(JeeslMessageHandler.class);

	private JeeslTranslationBean<L,D,LOC> bTranslation;
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
	
//	@Override
	public <T extends EjbWithId> void growlSuccessSaved(T t)
	{
		String summary;
		String detail;
		if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"jeeslFmSuccess");
			detail = bTranslation.get(localeCode,"jeeslFmObjectSaved");
		}
		else
		{
			summary = "** SUCCESS **";
			detail = "** Object Saved **";
		}
		
		logger.info("growlSuccessSaved "+t.toString());
		fcm.info("growl",summary,detail);
	}
}