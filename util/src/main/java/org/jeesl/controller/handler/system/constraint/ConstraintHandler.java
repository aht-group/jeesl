package org.jeesl.controller.handler.system.constraint;

import java.util.List;

import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.factory.txt.system.constraint.TxtConstraintFactory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstraintHandler <CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>>
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(ConstraintHandler.class);

	private final JeeslConstraintsBean<CONSTRAINT> bConstraint;
	private final TxtConstraintFactory<CONSTRAINT> tf;
	
	public static <CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>> ConstraintHandler<CONSTRAINT> instance(JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		return new ConstraintHandler<>(bConstraint);
	}
	
	private ConstraintHandler(JeeslConstraintsBean<CONSTRAINT> bConstraint)
	{
		this.bConstraint=bConstraint;
		tf = TxtConstraintFactory.instance("en");
	}
	
	public <CID extends Enum<CID>> void silent(List<CONSTRAINT> constraints, CID cId)
	{
		
	}
	
	public <CID extends Enum<CID>> void warn(Logger caller, List<CONSTRAINT> constraints, Class<?> c, CID cId)
	{
		CONSTRAINT constraint = bConstraint.getSilent(c,cId);
		constraints.add(constraint);
		logger.warn(tf.debug(constraint));
	}
}