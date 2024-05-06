package org.jeesl.controller.provider;

import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacadeConstraintProvider <CONSTRAINT extends JeeslConstraint<?,?,?,?,CONSTRAINT,?,?,?>>
									implements JeeslConstraintsBean<CONSTRAINT>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(FacadeConstraintProvider.class);
	
	private final JeeslSystemConstraintFacade<?,?,?,?,?,CONSTRAINT,?,?,?,?> fConstraint;
	
	public FacadeConstraintProvider(JeeslSystemConstraintFacade<?,?,?,?,?,CONSTRAINT,?,?,?,?> fConstraint)
	{
		this.fConstraint=fConstraint;
	}

	@Override
	public String getMessage(String category, String scope, String code, String lang) {
		logger.error("NYI");
		return null;
	}

	@Override
	public ConstraintScope getScope(String category, String scope, String lang) {
		logger.error("NYI");
		return null;
	}

	@Override
	public <SID extends Enum<SID>, CID extends Enum<CID>> CONSTRAINT getSilent(SID sId, CID cId) {
		logger.error("NYI");
		return null;
	}

	@Override
	public <CID extends Enum<CID>> CONSTRAINT get(Class<?> cScope, CID cId) throws JeeslNotFoundException
	{
		
		logger.error("NYI");
		return null;
	}

	@Override
	public <CID extends Enum<CID>> CONSTRAINT getSilent(Class<?> cScope, CID cId)
	{
		CONSTRAINT c = fConstraint.fSystemConstraint(cScope,cId);
		return c;
	}

	@Override
	public <S extends JeeslStatus<?,?,S>> CONSTRAINT getSilent(Class<?> cScope, S status)
	{
		logger.error("NYI");
		return null;
	}

	@Override
	public void update(CONSTRAINT constraint) {
		logger.error("NYI");
		
	}

	@Override
	public void ping() {
		logger.error("NYI");
		
	}
}