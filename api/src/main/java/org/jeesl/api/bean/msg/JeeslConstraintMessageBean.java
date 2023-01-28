package org.jeesl.api.bean.msg;

import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

public interface JeeslConstraintMessageBean<L extends JeeslLang, D extends JeeslDescription,
											ALGCAT extends JeeslStatus<L,D,ALGCAT>,
											ALGO extends JeeslConstraintAlgorithm<L,D,ALGCAT>,
											SCOPE extends JeeslConstraintScope<L,D,CONCAT>,
											CONCAT extends JeeslStatus<L,D,CONCAT>,
											CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
											LEVEL extends JeeslStatus<L,D,LEVEL>,
											TYPE extends JeeslStatus<L,D,TYPE>,
											RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
				extends JeeslFacesMessageBean
{
	<FID extends Enum<FID>> void show(FID fId, CONSTRAINT constraint);
	<FID extends Enum<FID>, SID extends Enum<SID>, CID extends Enum<CID>> void show(FID fId, SID sId, CID cId);
}