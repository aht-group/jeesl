package org.jeesl.web.mbean.prototype.system.constraint;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslConstraintMessageBean;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.jx.util.FacesContextMessage;
import org.jeesl.web.mbean.system.AbstractMessageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConstraintMessageBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CONSTRAINT extends JeeslConstraint<L,D,?,?,CONSTRAINT,LEVEL,?,?>,
											LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>>
							implements JeeslFacesMessageBean,JeeslConstraintMessageBean<CONSTRAINT>
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(AbstractConstraintMessageBean.class);
	
	private final static boolean debugOnInfo=true;
	
	
	
	private JeeslConstraintsBean<CONSTRAINT> bConstraint;
//	private JeeslTranslationBean<L,D,LOC> bTranslation;
	protected final JeeslFacesContextMessage fcm;
	
	private String localeCode;
	
	public AbstractConstraintMessageBean(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	protected void postConstruct(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		this.localeCode=localeCode;
//		this.bTranslation=bTranslation;
		this.bConstraint=bConstraint;
	}
	
	@Override public <FID extends Enum<FID>> void show(FID fId, CONSTRAINT constraint)
	{
		if(debugOnInfo)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Show ").append(constraint.toString());
			sb.append(" in locale "+localeCode);
			sb.append(" for FacesId:");if(fId!=null) {sb.append(fId.toString());}else {sb.append("null");}
			sb.append(" ").append(constraint.getLevel().getName().get(localeCode).getLang());
			sb.append(" ").append(constraint.getDescription().get(localeCode).getLang());
			logger.info(sb.toString());
		}
		
		fcm.error(fId, constraint.getLevel().getName().get(localeCode).getLang(), constraint.getDescription().get(localeCode).getLang());
	}
	
	@Override public <FID extends Enum<FID>, SID extends Enum<SID>, CID extends Enum<CID>> void show(FID fId, SID sId, CID cId)
	{
		if(debugOnInfo)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SID").append(sId.toString());
			sb.append("CID").append(cId.toString());
			logger.info(sb.toString());
		}
		
		CONSTRAINT c = bConstraint.getSilent(sId,cId);
		if(c!=null) {show(fId,c);}
		else
		{
			logger.error("Constraint not found");
			fcm.error(fId, "ERROR", "Constraint not found");
		}
	}
}