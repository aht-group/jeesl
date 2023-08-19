package org.jeesl.interfaces.model.module.workflow.msg;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;

public interface JeeslWorkflowStageNotification <AS extends JeeslWorkflowStage<?,?,?,?,?,?,?>,
												MT extends JeeslIoTemplate<?,?,?,?,?,?>,
												MC extends JeeslTemplateChannel<?,?,MC,?>,
												SR extends JeeslSecurityRole<?,?,?,?,?,?,?>,
												RE extends JeeslRevisionEntity<?,?,?,?,?,?>
									>
		extends JeeslWorkflowNotification<MT,MC,SR,RE>,EjbWithPositionVisible
{
	public static enum Attributes{stage}
	
	AS getStage();
	void setStage(AS stage);
	
	int getOverdueHours();
	void setOverdueHours(int overdueHours);
}