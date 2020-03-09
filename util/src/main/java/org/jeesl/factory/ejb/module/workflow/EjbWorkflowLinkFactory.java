package org.jeesl.factory.ejb.module.workflow;

import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowLinkFactory<RE extends JeeslRevisionEntity<?,?,?,?,?,?>,
									AL extends JeeslWorkflowLink<AW,RE>,
									AW extends JeeslWorkflow<?,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowLinkFactory.class);
	
	final Class<AL> cLink;
    
	public EjbWorkflowLinkFactory(final Class<AL> cLink)
	{       
        this.cLink = cLink;
	}
	    
	public AL build(RE entity, AW workflow, JeeslWithWorkflow<AW> object)
	{
		AL ejb = null;
		try
		{
			ejb = cLink.newInstance();
			ejb.setEntity(entity);
			ejb.setWorkflow(workflow);
			ejb.setRefId(object.getId());
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}