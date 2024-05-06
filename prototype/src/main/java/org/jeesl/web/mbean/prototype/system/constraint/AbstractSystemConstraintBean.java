package org.jeesl.web.mbean.prototype.system.constraint;

import java.io.Serializable;
import java.util.Comparator;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.controller.util.comparator.ejb.system.constraint.SystemConstraintAlgorithmComparator;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.interfaces.bean.sb.bean.SbToggleBean;
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
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSystemConstraintBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
													GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
													ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
													SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
													CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
													CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,LEVEL,TYPE>,
													LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
													TYPE extends JeeslConstraintType<L,D,TYPE,?>,
													RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbToggleBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractSystemConstraintBean.class);

	protected JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint;
	protected final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint;

	protected final Comparator<ALGORITHM> cpAlgorithm;
	
	public AbstractSystemConstraintBean(ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint)
	{
		super(fbConstraint.getClassL(),fbConstraint.getClassD());
		this.fbConstraint=fbConstraint;
		
		cpAlgorithm = (new SystemConstraintAlgorithmComparator<ALGORITHM,GROUP>()).factory(SystemConstraintAlgorithmComparator.Type.position);
	}
	
	protected void initConstraint(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
									JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fConstraint=fConstraint;
	}
}