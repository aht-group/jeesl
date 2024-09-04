package org.jeesl.controller.web.c.system.constraint;

import java.io.Serializable;

import org.jeesl.controller.web.system.constraint.JeeslSystemConstraintDefinitionWebController;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.model.ejb.io.locale.IoDescription;
import org.jeesl.model.ejb.io.locale.IoLang;
import org.jeesl.model.ejb.io.locale.IoLocale;
import org.jeesl.model.ejb.system.constraint.algorithm.SystemConstraintAlgorithm;
import org.jeesl.model.ejb.system.constraint.algorithm.SystemConstraintAlgorithmGroup;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraint;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintCategory;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintLevel;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintScope;
import org.jeesl.model.ejb.system.constraint.core.SystemConstraintType;
import org.jeesl.model.ejb.system.constraint.resolution.SystemConstraintResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslSystemConstraintWc extends JeeslSystemConstraintDefinitionWebController<IoLang,IoDescription,IoLocale,SystemConstraintAlgorithmGroup,SystemConstraintAlgorithm,SystemConstraintScope,SystemConstraint,SystemConstraintCategory,SystemConstraintLevel,SystemConstraintType,SystemConstraintResolution>
					implements Serializable
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(JeeslSystemConstraintWc.class);

	public JeeslSystemConstraintWc(ConstraintFactoryBuilder<IoLang,IoDescription,SystemConstraintAlgorithmGroup,SystemConstraintAlgorithm,SystemConstraintScope,SystemConstraintCategory,SystemConstraint,SystemConstraintLevel,SystemConstraintType,SystemConstraintResolution> fbConstraint)
	{
		super(fbConstraint);
	}
}