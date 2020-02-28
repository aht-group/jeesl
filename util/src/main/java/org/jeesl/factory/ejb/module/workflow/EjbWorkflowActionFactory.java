package org.jeesl.factory.ejb.module.workflow;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowActionFactory<T extends JeeslWorkflowTransition<?,?,?,?,?,?>,
											AA extends JeeslWorkflowAction<T,AB,AO,RE,RA>,
											AB extends JeeslWorkflowBot<AB,?,?,?>,
											AO extends EjbWithId,
											RE extends JeeslRevisionEntity<?,?,?,?,RA,?>,
											RA extends JeeslRevisionAttribute<?,?,RE,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowActionFactory.class);
	
	final Class<AA> cAction;
    
	public EjbWorkflowActionFactory(final Class<AA> cAction)
	{       
        this.cAction = cAction;
	}
	    
	public AA build(T transition, List<AA> list)
	{
		AA ejb = null;
		try
		{
			ejb = cAction.newInstance();
			EjbPositionFactory.next(ejb,list);
			ejb.setTransition(transition);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}