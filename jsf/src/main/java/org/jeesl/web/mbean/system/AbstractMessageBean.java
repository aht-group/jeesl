package org.jeesl.web.mbean.system;

import java.util.Objects;

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

	public AbstractMessageBean(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	public void initJeesl(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation)
	{
		this.jeeslLocaleCode=localeCode;
		this.jeeslTranslationBean=bTranslation;
	}
	
//	@Override public void errorConstraintViolationDuplicateObject() {fcm.error(jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationDuplicateObject"),"");}
	@Override public <FID extends Enum<FID>> void errorConstraintViolationDuplicateObject(FID id) {fcm.error(id,jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationDuplicateObject"),"");}
	
	@Override public <FID extends Enum<FID>> void errorConstraintViolationInUse(FID id) {errorTextWithId(id,jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationInUse"));}
	
	@Override public <FID extends Enum<FID>> void errorText(FID id, String text)
	{
		if(Objects.nonNull(id)) {errorTextWithId(id,text);}
		else {errorTextWithId(null,text);}
	}
	
	private <FID extends Enum<FID>> void errorTextWithId(FID id, String text)
	{
		fcm.error(id, jeeslTranslationBean.get(jeeslLocaleCode, "fmError"), text);
	}
}