package org.jeesl.api.facade.system;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithm;
import org.jeesl.interfaces.model.system.constraint.algorithm.JeeslConstraintAlgorithmGroup;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;

public interface JeeslSystemConstraintFacade <L extends JeeslLang, D extends JeeslDescription,
									ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
									GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
									SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
									CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
									CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
									LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
									TYPE extends JeeslConstraintType<L,D,TYPE,?>,
									RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
			extends JeeslFacade
{	
	<E extends Enum<E>> CONSTRAINT fSystemConstraint(Class<?> c, E code);
	CONSTRAINT fSystemConstraint(SCOPE scope, String code) throws JeeslNotFoundException;
}