package org.jeesl.jsf.handler.system.constraint;

import org.jeesl.api.bean.msg.JeeslConstraintMessageBean;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslConstraintHandler <CONSTRAINT extends JeeslConstraint<?,?,?,?,CONSTRAINT,LEVEL,?,?>,
									LEVEL extends JeeslConstraintLevel<?,?,LEVEL,?>
									>
								implements JeeslConstraintMessageBean<CONSTRAINT>
{
	final static Logger logger = LoggerFactory.getLogger(JeeslConstraintHandler.class);
	
	private final JeeslFacesContextMessage fcm;
	
	private JeeslConstraintsBean<CONSTRAINT> bConstraint;
	
	private String localeCode;
	private boolean debugOnInfo = false;
	
	public JeeslConstraintHandler(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	public void postConstruct(String localeCode, JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		this.localeCode=localeCode;
		this.bConstraint=bConstraint;
	}
	
//	@Override
	public <FID extends Enum<FID>> void show(FID fId, CONSTRAINT constraint)
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
	
//	@Override
	public <FID extends Enum<FID>, SID extends Enum<SID>, CID extends Enum<CID>> void show(FID facesId, SID scopeId, CID constraintId)
	{
		if(debugOnInfo)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("SID").append(scopeId.toString());
			sb.append("CID").append(constraintId.toString());
			logger.info(sb.toString());
		}
		
		CONSTRAINT c = bConstraint.getSilent(scopeId,constraintId);
		if(c!=null) {show(facesId,c);}
		else
		{
			logger.error("Constraint not found");
			fcm.error(facesId, "ERROR", "Constraint not found");
		}
	}
}