package org.jeesl.api.bean.msg;

import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;

public interface JeeslConstraintMessageBean<CONSTRAINT extends JeeslConstraint<?,?,?,?,?,?>>
{
	<FID extends Enum<FID>> void show(FID fId, CONSTRAINT constraint);
	<FID extends Enum<FID>, SID extends Enum<SID>, CID extends Enum<CID>> void show(FID fId, SID sId, CID cId);
}