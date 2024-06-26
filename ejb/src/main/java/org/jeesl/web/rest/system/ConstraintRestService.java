package org.jeesl.web.rest.system;

import org.jeesl.api.facade.system.JeeslSystemConstraintFacade;
import org.jeesl.api.rest.rs.system.constraint.JeeslConstraintRestExport;
import org.jeesl.api.rest.rs.system.constraint.JeeslConstraintRestImport;
import org.jeesl.controller.monitoring.counter.DataUpdateTracker;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.system.ConstraintFactoryBuilder;
import org.jeesl.factory.ejb.system.constraint.EjbConstraintFactory;
import org.jeesl.factory.ejb.system.constraint.EjbConstraintScopeFactory;
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
import org.jeesl.model.xml.io.ssi.sync.DataUpdate;
import org.jeesl.model.xml.system.constraint.Constraint;
import org.jeesl.model.xml.system.constraint.ConstraintScope;
import org.jeesl.model.xml.system.constraint.Constraints;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConstraintRestService <L extends JeeslLang, D extends JeeslDescription,
									GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
									ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
									SCOPE extends JeeslConstraintScope<L,D,CONCAT>,
									CONCAT extends JeeslConstraintCategory<L,D,CONCAT,?>,
									CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CONCAT,LEVEL,TYPE>,
									LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
									TYPE extends JeeslConstraintType<L,D,TYPE,?>,
									RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
		extends AbstractJeeslRestHandler<L,D>
		implements JeeslConstraintRestExport,JeeslConstraintRestImport
{
	final static Logger logger = LoggerFactory.getLogger(ConstraintRestService.class);
	
	private final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint;
	private JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CONCAT,LEVEL,TYPE,RESOLUTION> fConstraint;
	
	private final EjbConstraintScopeFactory<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> efScope;
	private final EjbConstraintFactory<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> efConstraint;
	
	private ConstraintRestService(final String[] localeCodes, JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CONCAT,LEVEL,TYPE,RESOLUTION> fConstraint,
			final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint)
	{
		super(fConstraint,fbConstraint.getClassL(),fbConstraint.getClassD());
		this.fbConstraint=fbConstraint;
		
		this.fConstraint=fConstraint;

		efScope = new EjbConstraintScopeFactory<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>(cL,cD,fbConstraint.getClassScope(),fbConstraint.getClassConstraintCategory());
		efConstraint = new EjbConstraintFactory<L,D,SCOPE,CONCAT,CONSTRAINT,LEVEL,TYPE,RESOLUTION>(cL,cD,fbConstraint.getClassConstraint(),fbConstraint.getClassConstraintType());
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription,
						GROUP extends JeeslConstraintAlgorithmGroup<L,D,GROUP,?>,
						ALGORITHM extends JeeslConstraintAlgorithm<L,D,GROUP>,
						SCOPE extends JeeslConstraintScope<L,D,CATEGORY>,
						CATEGORY extends JeeslConstraintCategory<L,D,CATEGORY,?>,
						CONSTRAINT extends JeeslConstraint<L,D,SCOPE,CATEGORY,LEVEL,TYPE>,
						LEVEL extends JeeslConstraintLevel<L,D,LEVEL,?>,
						TYPE extends JeeslConstraintType<L,D,TYPE,?>,
						RESOLUTION extends JeeslConstraintResolution<L,D,CONSTRAINT>>
			ConstraintRestService<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>
					factory(String[] localeCodes,
							JeeslSystemConstraintFacade<L,D,ALGORITHM,GROUP,SCOPE,CONSTRAINT,CATEGORY,LEVEL,TYPE,RESOLUTION> fConstraint,
							final ConstraintFactoryBuilder<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION> fbConstraint)
	{
		return new ConstraintRestService<L,D,GROUP,ALGORITHM,SCOPE,CATEGORY,CONSTRAINT,LEVEL,TYPE,RESOLUTION>(localeCodes,fConstraint,fbConstraint);
	}
	
	@Override public Container exportSystemConstraintCategories() {return xfContainer.build(fConstraint.allOrderedPosition(fbConstraint.getClassConstraintCategory()));}
	@Override public Container exportSystemConstraintTypes() {return xfContainer.build(fConstraint.allOrderedPosition(fbConstraint.getClassConstraintType()));}
	@Override public Container exportSystemConstraintLevel() {return xfContainer.build(fConstraint.allOrderedPosition(fbConstraint.getClassConstraintLevel()));}
	@Override public Constraints exportConstraints()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override public DataUpdate importSystemConstraintCategories(Container categories) {return super.importStatus(fbConstraint.getClassConstraintCategory(),categories,null);}
	@Override public DataUpdate importSystemConstraintTypes(Container categories) {return super.importStatus(fbConstraint.getClassConstraintType(),categories,null);}

	@Override public DataUpdate importConstraints(Constraints constraints)
	{
		DataUpdateTracker dut = new DataUpdateTracker(true);
		for(ConstraintScope xScope : constraints.getConstraintScope())
		{
			try
			{
				SCOPE eScope = efScope.importOrUpdate(fConstraint, xScope);
				dut.createSuccess(fbConstraint.getClassScope());
				
				for(Constraint xConstraint : xScope.getConstraint())
				{
					try
					{
						efConstraint.importOrUpdate(fConstraint,eScope,xConstraint);
						dut.createSuccess(fbConstraint.getClassConstraint());
					}
					catch (JeeslNotFoundException e) {dut.createFail(fbConstraint.getClassConstraint(),e);}
					catch (JeeslConstraintViolationException e) {dut.createFail(fbConstraint.getClassConstraint(),e);}
					catch (JeeslLockingException e) {dut.createFail(fbConstraint.getClassConstraint(),e);}
				}
				
			}
			catch (JeeslNotFoundException e) {dut.createFail(fbConstraint.getClassScope(),e);}
			catch (JeeslConstraintViolationException e) {dut.createFail(fbConstraint.getClassScope(),e);}
			catch (JeeslLockingException e) {dut.createFail(fbConstraint.getClassScope(),e);}
		}
		return dut.toDataUpdate();
	}
}