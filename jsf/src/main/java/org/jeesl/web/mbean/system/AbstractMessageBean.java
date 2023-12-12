package org.jeesl.web.mbean.system;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMessageBean <L extends JeeslLang, D extends JeeslDescription,LOC extends JeeslLocale<L,D,LOC,?>>
					implements JeeslFacesMessageBean
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(AbstractMessageBean.class);

	private JeeslTranslationBean<L,D,LOC> jeeslTranslationBean;
	protected String jeeslLocaleCode;
	
	protected final JeeslFacesContextMessage fcm;

	private AbstractMessageBean(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	public void initJeesl(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation)
	{
		this.jeeslLocaleCode=localeCode;
		this.jeeslTranslationBean=bTranslation;
	}
}