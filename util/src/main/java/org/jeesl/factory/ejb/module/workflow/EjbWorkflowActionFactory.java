package org.jeesl.factory.ejb.module.workflow;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowActionFactory<T extends JeeslWorkflowTransition<?,?,?,?,?,?,?>,
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
			ejb = cAction.getDeclaredConstructor().newInstance();
			EjbPositionFactory.next(ejb,list);
			ejb.setTransition(transition);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
		
		return ejb;
	}
}