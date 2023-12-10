package org.jeesl.web.mbean.prototype.system.constraint;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslConstraintMessageBean;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.jsf.jx.util.FacesContextMessage;
import org.jeesl.web.mbean.system.AbstractMessageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractConstraintMessageBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											CONSTRAINT extends JeeslConstraint<L,D,?,?,CONSTRAINT,LEVEL,?,?>,
											LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>>
							extends AbstractMessageBean<L,D,LOC> implements JeeslConstraintMessageBean<CONSTRAINT>
{
	private static final long serialVersionUID = 1;
	final static Logger logger = LoggerFactory.getLogger(AbstractConstraintMessageBean.class);
	
	private final static boolean debugOnInfo=true;
	
	private JeeslConstraintsBean<CONSTRAINT> bConstraint;
	
	protected void postConstruct(String localeCode, JeeslTranslationBean<L,D,LOC> bTranslation, JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		super.initJeesl(localeCode,bTranslation);
		this.bConstraint=bConstraint;
	}
	
	@Override public <FID extends Enum<FID>> void show(FID fId, CONSTRAINT constraint)
	{
		if(debugOnInfo)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("Show ").append(constraint.toString());
			sb.append(" in locale "+jeeslLocaleCode);
			sb.append(" for FacesId:");if(fId!=null) {sb.append(fId.toString());}else {sb.append("null");}
			sb.append(" ").append(constraint.getLevel().getName().get(jeeslLocaleCode).getLang());
			sb.append(" ").append(constraint.getDescription().get(jeeslLocaleCode).getLang());
			logger.info(sb.toString());
		}
		String facesId = null;
		if(fId!=null) {facesId=fId.toString();}
		
		FacesContextMessage.error(facesId, constraint.getLevel().getName().get(jeeslLocaleCode).getLang(), constraint.getDescription().get(jeeslLocaleCode).getLang());
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
		if(c!=null){show(fId,c);}
		else
		{
			logger.error("Constraint not found");
			FacesContextMessage.error(fId.toString(), "ERROR", "Constraint not found");
		}
	}
}