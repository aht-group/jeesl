package org.jeesl.factory.ejb.system.constraint;

import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
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
import org.jeesl.model.xml.system.constraint.Constraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbConstraintFactory <L extends JeeslLang, D extends JeeslDescription,
										SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
										CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
										CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,LEVEL,TYPE>,
										LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
										TYPE extends JeeslConstraintType<L,D,TYPE,?>,
										RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbConstraintFactory.class);
	
	private final Class<CONSTRAINT> cConstraint;
	private final Class<TYPE> cType;

	private final JeeslDbLangUpdater<CONSTRAINT,L> dbuLang;
	private final JeeslDbDescriptionUpdater<CONSTRAINT,D> dbuDescription;
	
	public EjbConstraintFactory(final Class<L> cL, final Class<D> cD, final Class<CONSTRAINT> cConstraint, final Class<TYPE> cType)
	{
        this.cConstraint = cConstraint;
        this.cType = cType;
        
        dbuLang = JeeslDbLangUpdater.factory(cConstraint,cL);
        dbuDescription = JeeslDbDescriptionUpdater.factory(cConstraint,cD);
	}
	
	public CONSTRAINT build(SCOPE scope, TYPE type)
	{
		CONSTRAINT ejb = null;
		try
		{
			ejb = cConstraint.newInstance();
			ejb.setPosition(0);
			ejb.setScope(scope);
			ejb.setType(type);
			
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public CONSTRAINT importOrUpdate(JeeslSystemConstraintFacade<L,D,?,?,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint, SCOPE eScope, Constraint xConstraint) throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		CONSTRAINT eConstraint;	
		try {eConstraint = fConstraint.fSystemConstraint(eScope,xConstraint.getCode());}
		catch (JeeslNotFoundException e) {eConstraint = this.build(eScope,null);}
		eConstraint.setType(fConstraint.fByCode(cType,xConstraint.getType().getCode()));
		eConstraint = this.update(eConstraint, xConstraint);
		eConstraint = fConstraint.save(eConstraint);
		return this.updateLD(fConstraint,eConstraint,xConstraint);
	}
	
	public CONSTRAINT update(CONSTRAINT eConstraint, Constraint xConstraint)
	{
		eConstraint.setCode(xConstraint.getCode());
		eConstraint.setPosition(0);
		return eConstraint;
	}
	
	public CONSTRAINT updateLD(JeeslFacade fUtils, CONSTRAINT eConstraint, Constraint xConstraint) throws JeeslConstraintViolationException, JeeslLockingException
	{
		eConstraint = dbuLang.handle(fUtils, eConstraint, xConstraint.getLangs());
		eConstraint = fUtils.save(eConstraint);
		eConstraint = dbuDescription.handle(fUtils, eConstraint, xConstraint.getDescriptions());
		eConstraint = fUtils.save(eConstraint);
		return eConstraint;
	}
	
	public CONSTRAINT updateLD(JeeslFacade fUtils, CONSTRAINT eConstraint, String[] localeCodes)
	{
		eConstraint = dbuLang.handle(fUtils, eConstraint, localeCodes);
		eConstraint = dbuDescription.handle(fUtils, eConstraint, localeCodes);
		return eConstraint;
	}
}