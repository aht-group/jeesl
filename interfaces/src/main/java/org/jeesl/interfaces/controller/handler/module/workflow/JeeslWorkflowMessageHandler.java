package org.jeesl.interfaces.controller.handler.module.workflow;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowNotification;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;

public interface JeeslWorkflowMessageHandler<WS extends JeeslWorkflowStage<?,?,?,?,?,?,?>,
											
											SR extends JeeslSecurityRole<?,?,?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>,
											MT extends JeeslIoTemplate<?,?,?,?,MD,?>,
											MC extends JeeslTemplateChannel<?,?,MC,?>,
											MD extends JeeslIoTemplateDefinition<?,MC,MT>,
											WF extends JeeslWorkflow<?,?,?,USER>,
											WY extends JeeslWorkflowActivity<?,WF,?,?,USER>,
											USER extends JeeslUser<SR>>
							extends Serializable
{
	String localeCode(USER user);
	String headerPrefix();
	
	List<USER> getRecipients(JeeslWithWorkflow<WF> workflowOwner, JeeslWorkflowNotification<MT,MC,SR,RE> communication);
	
	EmailAddress senderEmail(WY activity);
	EmailAddress senderEmail(WS stage);
	EmailAddress recipientEmail(USER user);
	
	void completeModel(JeeslWithWorkflow<WF> entity, JeeslWorkflowNotification<MT,MC,SR,RE> communication, String localeCode, Map<String,Object> model);
	
	void spool(Mails mails) throws JeeslConstraintViolationException, JeeslNotFoundException;
	void spool(Mail mail) throws JeeslConstraintViolationException, JeeslNotFoundException;
	List<MD> getDefinitions(MT template);
}