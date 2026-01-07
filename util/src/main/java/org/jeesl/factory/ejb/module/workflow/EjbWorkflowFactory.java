package org.jeesl.factory.ejb.module.workflow;

import java.lang.reflect.InvocationTargetException;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowFactory<WP extends JeeslWorkflowProcess<?,?,?,WS>,
								WS extends JeeslWorkflowStage<?,?,WP,?,?,?,?>,
								WL extends JeeslWorkflowLink<WF,?>,
								WF extends JeeslWorkflow<WP,WS,?,?>

>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowFactory.class);
	
	final Class<WF> cWorkflow;
    
	public EjbWorkflowFactory(final Class<WF> cWorkflow)
	{       
        this.cWorkflow = cWorkflow;
	}
	    
	public WF build(WP process)
	{
		WF ejb = null;
		try
		{
			ejb = cWorkflow.getDeclaredConstructor().newInstance();
			ejb.setProcess(process);
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {e.printStackTrace();}
		
		return ejb;
	}
	
	public void delete(JeeslWorkflowFacade<WP,WS,?,?,?,?,?,WL,WF,?,?,?> facade,WF wf)
	{
		logger.info("Deleting {}",wf.toString());
	}
}