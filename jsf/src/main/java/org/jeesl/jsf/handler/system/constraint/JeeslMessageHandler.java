package org.jeesl.jsf.handler.system.constraint;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
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
								implements JeeslFacesMessageBean
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(JeeslMessageHandler.class);

	private JeeslTranslationBean<L,D,LOC> bTranslation;
	private JeeslTranslationProvider<LOC> tp;
	
	private final JeeslFacesContextMessage fcm;
	
	private String localeCode;
	
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
	
	@Override public <T extends EjbWithId> void growlSaved(T t)
	{
		String summary = null;
		String detail= null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summarySuccess);
			detail = tp.toDescription(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Detail.detailSaved);
		}
		else if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"jeeslFmSuccess");
			detail = bTranslation.get(localeCode,"jeeslFmObjectSaved");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "** SUCCESS **";}
		if(ObjectUtils.isEmpty(detail)) {detail = "** Object Saved **";}
		
		fcm.info(JeeslFacesContextMessage.Faces.growl,summary,detail);
	}
	
	@Override public <T extends EjbWithId> void growlDeleted(T t)
	{
		String summary = null;
		String detail= null;
		
//		if(Objects.nonNull(tp))
//		{
//			summary = tp.toLabel(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summarySuccess);
//			detail = tp.toDescription(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Detail.detailSaved);
//		}
		if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"jeeslFmSuccess");
			detail = bTranslation.get(localeCode,"fmObjectRemoved");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "** SUCCESS **";}
		if(ObjectUtils.isEmpty(detail)) {detail = "** Object Deleted **";}
		
		fcm.info(JeeslFacesContextMessage.Faces.growl,summary,detail);
	}
	
	@Override public void growlInfo(String text)
	{
		String summary = null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summaryInfo);
		}
		if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"aupFmInfo");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "INFO";}
		
		fcm.info(JeeslFacesContextMessage.Faces.growl,summary,text);
	}
	
	@Override public void growlError(String text)
	{
		String summary = null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summaryError);
		}
		if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"jeeslFmError");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "ERROR";}
		
		fcm.info(JeeslFacesContextMessage.Faces.growl,summary,text);
	}
	
	@Override public <FID extends Enum<FID>> void error(FID fid, String text)
	{
		String summary = null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summaryError);
		}
		if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"fmError");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "ERROR";}
		
		fcm.info(fid,summary,text);
	}
	
	
	@Override public <FID extends Enum<FID>> void constraintDuplicate(FID id)
	{
		String summary = null;
		String detail= null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summaryError);
			detail = tp.toDescription(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Detail.detailDuplicate);
		}
		else if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"fmError");
			detail = bTranslation.get(localeCode,"fmConstraintViolationDuplicateObject");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "** ERROR **";}
		if(ObjectUtils.isEmpty(detail)) {detail = "** Duplicate **";}
		
		fcm.info(id,summary,detail);
	}
	
	@Override public <FID extends Enum<FID>> void constraintInUse(FID id)
	{
		String summary = null;
		String detail= null;
		
		if(Objects.nonNull(tp))
		{
			summary = tp.tAttribute(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Summary.summaryError);
			detail = tp.toDescription(localeCode, FacesContextMessage.class, JeeslFacesContextMessage.Detail.detailInUse);
		}
		else if(Objects.nonNull(bTranslation))
		{
			summary = bTranslation.get(localeCode,"fmError");
			detail = bTranslation.get(localeCode,"fmConstraintViolationInUse");
		}
		
		if(ObjectUtils.isEmpty(summary)) {summary = "** ERROR **";}
		if(ObjectUtils.isEmpty(detail)) {detail = "** In Use **";}
		
		fcm.error(id,summary,detail);		
	}
}