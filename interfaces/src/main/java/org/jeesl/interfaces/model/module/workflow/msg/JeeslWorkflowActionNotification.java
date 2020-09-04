package org.jeesl.interfaces.model.module.workflow.msg;

import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;

public interface JeeslWorkflowActionNotification <WT extends JeeslWorkflowTransition<?,?,?,?,?,?,?>,
											MT extends JeeslIoTemplate<?,?,?,?,?,?>,
											MC extends JeeslTemplateChannel<?,?,MC,?>,
											SR extends JeeslSecurityRole<?,?,?,?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>
									>
		extends JeeslWorkflowNotification<MT,MC,SR,RE>
				
{
	public static enum Attributes{transition}
	
	WT getTransition();
	void setTransition(WT transition);
}