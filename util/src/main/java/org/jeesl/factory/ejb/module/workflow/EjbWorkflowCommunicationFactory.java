package org.jeesl.factory.ejb.module.workflow;

import java.util.List;

import org.jeesl.factory.ejb.util.EjbPositionFactory;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowActionNotification;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbWorkflowCommunicationFactory<WT extends JeeslWorkflowTransition<?,?,?,?,?,SR,?>,
											WC extends JeeslWorkflowActionNotification<WT,MT,MC,SR,RE>,
											MT extends JeeslIoTemplate<?,?,?,?,?,?>,
											MC extends JeeslTemplateChannel<?,?,MC,?>,
											SR extends JeeslSecurityRole<?,?,?,?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>
>
{
	final static Logger logger = LoggerFactory.getLogger(EjbWorkflowCommunicationFactory.class);
	
	final Class<WC> cCommunication;
    
	public EjbWorkflowCommunicationFactory(final Class<WC> cCommunication)
	{       
        this.cCommunication = cCommunication;
	}
	    
	public WC build(WT transition, List<WC> list)
	{
		WC ejb = null;
		try
		{
			ejb = cCommunication.newInstance();
			EjbPositionFactory.next(ejb,list);
			ejb.setTransition(transition);
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}