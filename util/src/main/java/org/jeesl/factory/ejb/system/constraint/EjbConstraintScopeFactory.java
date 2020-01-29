package org.jeesl.factory.ejb.system.constraint;

import org.jeesl.controller.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.constraint.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.JeeslConstraintScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.interfaces.facade.UtilsFacade;
import net.sf.ahtutils.interfaces.model.status.UtilsDescription;
import net.sf.ahtutils.interfaces.model.status.UtilsLang;
import net.sf.ahtutils.interfaces.model.status.UtilsStatus;
import net.sf.ahtutils.xml.system.ConstraintScope;

public class EjbConstraintScopeFactory <L extends UtilsLang, D extends UtilsDescription,
										SCOPE extends JeeslConstraintScope<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
										CATEGORY extends UtilsStatus<CATEGORY,L,D>,
										CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>,
										LEVEL extends UtilsStatus<LEVEL,L,D>,
										TYPE extends UtilsStatus<TYPE,L,D>,
										RESOLUTION extends JeeslConstraintResolution<L,D,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbConstraintScopeFactory.class);
	
	private final Class<SCOPE> cScope;
	private final Class<CATEGORY> cCategory;

	private final JeeslDbLangUpdater<SCOPE,L> dbuLang;
	private final JeeslDbDescriptionUpdater<SCOPE,D> dbuDescription;
	
	public EjbConstraintScopeFactory(final Class<L> cL, final Class<D> cD, final Class<SCOPE> cScope, Class<CATEGORY> cCategory)
	{
        this.cScope = cScope;
        this.cCategory = cCategory;
        dbuLang = JeeslDbLangUpdater.factory(cScope,cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cScope,cD);
	}
	
	public SCOPE build(CATEGORY category)
	{
		SCOPE ejb = null;
		try
		{
			ejb = cScope.newInstance();
			ejb.setPosition(0);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public SCOPE importOrUpdate(UtilsFacade fUtils, ConstraintScope xScope) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		SCOPE eScope;	
		try {eScope = fUtils.fByCode(cScope,xScope.getCode());}
		catch (JeeslNotFoundException e) {eScope = this.build(null);}
		eScope.setCategory(fUtils.fByCode(cCategory,xScope.getCategory()));
		eScope = this.update(eScope, xScope);
		eScope = fUtils.save(eScope);
		return this.updateLD(fUtils,eScope,xScope);
	}
	
	private SCOPE update(SCOPE eScope, ConstraintScope xScope)
	{
		eScope.setCode(xScope.getCode());
		eScope.setPosition(0);
		return eScope;
	}
	
	private SCOPE updateLD(UtilsFacade fUtils, SCOPE eScope, ConstraintScope xScope) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eScope = dbuLang.handle(fUtils, eScope, xScope.getLangs());
		eScope = fUtils.save(eScope);
		eScope = dbuDescription.handle(fUtils, eScope, xScope.getDescriptions());
		eScope = fUtils.save(eScope);
		return eScope;
	}
	
	public SCOPE updateLD(UtilsFacade fUtils, SCOPE eScope, String[] localeCodes)
	{
		eScope = dbuLang.handle(fUtils, eScope, localeCodes);
		eScope = dbuDescription.handle(fUtils, eScope, localeCodes);
		return eScope;
	}
}