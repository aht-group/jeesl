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
	
	public AbstractMessageBean(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	public void initJeesl(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation)
	{
		this.jeeslLocaleCode=localeCode;
		this.jeeslTranslationBean=bTranslation;
	}
	

	@Override public void growlSuccessRemoved(Object o) {fcm.info(JeeslFacesContextMessage.Faces.growl, jeeslTranslationBean.get(jeeslLocaleCode,"jeeslFmSuccess"), jeeslTranslationBean.get(jeeslLocaleCode,"fmObjectRemoved"));}
	
	@Override public void growlError(String key) {fcm.info(JeeslFacesContextMessage.Faces.growl, jeeslTranslationBean.get(jeeslLocaleCode,"jeeslFmError"), jeeslTranslationBean.get(jeeslLocaleCode, key));}
	@Override public void growlSuccess(String key) {fcm.info(JeeslFacesContextMessage.Faces.growl, jeeslTranslationBean.get(jeeslLocaleCode,"jeeslFmSuccess"), jeeslTranslationBean.get(jeeslLocaleCode, key));}
	@Override public void growlInfoText(String text) {fcm.error("growl", jeeslTranslationBean.get(jeeslLocaleCode, "fmInfo"), text);}
	
	@Override public void errorConstraintViolationDuplicateObject() {fcm.error(jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationDuplicateObject"),"");}
	@Override public <E extends Enum<E>> void errorConstraintViolationDuplicateObject(E id) {fcm.error(id.toString(),jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationDuplicateObject"),"");}
	
	@Override public void errorConstraintViolationInUse() {errorConstraintViolationInUse(null);}
	@Override public void errorConstraintViolationInUse(String id) {errorText(id,jeeslTranslationBean.get(jeeslLocaleCode,"fmConstraintViolationInUse"));}
	
	@Override public void errorText(String text) {errorTextWithId(null,text);}
	@Override public <E extends Enum<E>> void errorText(E id, String text) {errorTextWithId(id.toString(),text);}
	
	public void errorText(String id, String text) {errorTextWithId(id,text);}
	
	private void errorTextWithId(String id, String text)
	{
		fcm.error(id, jeeslTranslationBean.get(jeeslLocaleCode, "fmError"), text);
	}
}