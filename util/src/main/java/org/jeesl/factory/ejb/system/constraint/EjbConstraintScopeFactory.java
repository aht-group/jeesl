package org.jeesl.factory.ejb.system.constraint;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.controller.io.db.updater.JeeslDbDescriptionUpdater;
import org.jeesl.controller.io.db.updater.JeeslDbLangUpdater;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintCategory;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintLevel;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintResolution;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintScope;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraintType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbConstraintScopeFactory <L extends JeeslLang, D extends JeeslDescription,
										SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
										CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
										CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,LEVEL,TYPE>,
										LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
										TYPE extends JeeslConstraintType<L,D,TYPE,?>,
										RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
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
	
	private SCOPE build()
	{
		SCOPE ejb = null;
		try
		{
			ejb = cScope.getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		catch (IllegalArgumentException e) {e.printStackTrace();}
		catch (InvocationTargetException e) {e.printStackTrace();}
		catch (NoSuchMethodException e) {e.printStackTrace();}
		catch (SecurityException e) {e.printStackTrace();}
		return ejb;
	}
	
	public SCOPE build(CATEGORY category)
	{
		SCOPE ejb = this.build();
		ejb.setPosition(0);
		
		return ejb;
	}
	
	public SCOPE importOrUpdate(JeeslFacade fUtils, ConstraintScope xScope) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
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
	
	private SCOPE updateLD(JeeslFacade fUtils, SCOPE eScope, ConstraintScope xScope) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eScope = dbuLang.handle(fUtils, eScope, xScope.getLangs());
		eScope = fUtils.save(eScope);
		eScope = dbuDescription.handle(fUtils, eScope, xScope.getDescriptions());
		eScope = fUtils.save(eScope);
		return eScope;
	}
	
	public SCOPE updateLD(JeeslFacade fUtils, SCOPE eScope, String[] localeCodes)
	{
		eScope = dbuLang.handle(fUtils, eScope, localeCodes);
		eScope = dbuDescription.handle(fUtils, eScope, localeCodes);
		return eScope;
	}
}