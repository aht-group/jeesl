package org.jeesl.factory.ejb.module.workflow;

import java.util.Date;

import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowActivityFactory<WT extends JeeslWorkflowTransition<?,?,?,?,?,?>,
										AW extends JeeslWorkflow<?,?,AY>,
										AY extends JeeslWorkflowActivity<WT,AW,?,USER>,
										USER extends JeeslUser<?>

>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowActivityFactory.class);
	
	final Class<AY> cActivity;
    
	public EjbWorkflowActivityFactory(final Class<AY> cActivity)
	{       
        this.cActivity = cActivity;
	}
	    
	public AY build(AW workflow, WT transition, USER user)
	{
		return build(workflow,transition,user,new Date());
	}
	
	public AY build(AW workflow, WT transition, USER user, Date date)
	{
		AY ejb = null;
		try
		{
			ejb = cActivity.newInstance();
			ejb.setWorkflow(workflow);
			ejb.setTransition(transition);
			ejb.setRecord(date);
			ejb.setUser(user);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}