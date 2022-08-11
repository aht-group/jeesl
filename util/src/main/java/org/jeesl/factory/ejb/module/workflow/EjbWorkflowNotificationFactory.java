package org.jeesl.factory.ejb.module.workflow;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowNotificationFactory<WS extends JeeslWorkflowStage<?,?,?,?,?,?,?>,
											WSN extends JeeslWorkflowStageNotification<WS,MT,MC,SR,RE>,
											MT extends JeeslIoTemplate<?,?,?,?,?,?>,
											MC extends JeeslTemplateChannel<?,?,MC,?>,
											SR extends JeeslSecurityRole<?,?,?,?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowNotificationFactory.class);
	
	final Class<WSN> cCommunication;
    
	public EjbWorkflowNotificationFactory(final Class<WSN> cCommunication)
	{       
        this.cCommunication = cCommunication;
	}
	    
	public WSN build(WS stage, List<WSN> list)
	{
		WSN ejb = null;
		try
		{
			ejb = cCommunication.newInstance();
			EjbPositionFactory.next(ejb,list);
			ejb.setStage(stage);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}