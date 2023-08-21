package org.jeesl.factory.ejb.module.workflow;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowModificationLevel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStagePermission;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowPermissionFactory<AS extends JeeslWorkflowStage<?,?,?,?,WSP,?,?>,
											WSP extends JeeslWorkflowStagePermission<AS,?,WML,SR>,
											WML extends JeeslWorkflowModificationLevel<?,?,WML,?>,
											SR extends JeeslSecurityRole<?,?,?,?,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowPermissionFactory.class);
	
	private final Class<WSP> cPermission;
    
	public EjbWorkflowPermissionFactory(final Class<WSP> cPermission)
	{       
        this.cPermission = cPermission;
	}
	    
	public WSP build(AS stage, List<WSP> list)
	{
		WSP ejb = null;
		try
		{
			ejb = cPermission.newInstance();
			EjbPositionFactory.next(ejb,list);
			ejb.setStage(stage);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}