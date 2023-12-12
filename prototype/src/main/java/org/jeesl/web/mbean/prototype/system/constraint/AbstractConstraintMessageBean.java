package org.jeesl.web.mbean.prototype.system.constraint;

import org.jeesl.api.bean.msg.JeeslConstraintMessageBean;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.interfaces.bean.system.JeeslFacesContextMessage;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
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
	protected final JeeslFacesContextMessage fcm;
	
	private String localeCode;
	
	public AbstractConstraintMessageBean(JeeslFacesContextMessage fcm)
	{
		this.fcm=fcm;
	}
	
	protected void postConstruct(String localeCode, JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		this.localeCode=localeCode;
		this.bConstraint=bConstraint;
	}
	
	
	
	@Override public <FID extends Enum<FID>, SID extends Enum<SID>, CID extends Enum<CID>> void show(FID facesId, SID scopeId, CID constraintId)
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