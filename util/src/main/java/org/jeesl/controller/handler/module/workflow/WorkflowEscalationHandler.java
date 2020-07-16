package org.jeesl.controller.handler.module.workflow;

import java.util.List;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowMessageHandler;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowCommunication;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowDelegate;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowModificationLevel;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowPermissionType;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStagePermission;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStageType;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransitionType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.module.workflow.Workflow;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowEscalationHandler<L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<LOC,L,D>,
					WX extends JeeslWorkflowContext<L,D,WX,?>,
					WP extends JeeslWorkflowProcess<L,D,WX,WS>,
					WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
					WST extends JeeslWorkflowStageType<L,D,WST,?>,
					WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
					WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
					WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
					WT extends JeeslWorkflowTransition<L,D,WS,WTT,SR,?>,
					WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
					WC extends JeeslWorkflowCommunication<WT,MT,MC,SR,RE>,
					WA extends JeeslWorkflowAction<WT,AB,AO,RE,RA>,
					AB extends JeeslWorkflowBot<AB,L,D,?>,
					AO extends EjbWithId,
					MT extends JeeslIoTemplate<L,D,?,?,MD,?>,
					MC extends JeeslTemplateChannel<L,D,MC,?>,
					MD extends JeeslIoTemplateDefinition<D,MC,MT>,
					SR extends JeeslSecurityRole<L,D,?,?,?,?,USER>,
					RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
					RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
					WL extends JeeslWorkflowLink<WF,RE>,
					WF extends JeeslWorkflow<WP,WS,WY,USER>,
					WY extends JeeslWorkflowActivity<WT,WF,FRC,USER>,
					WD extends JeeslWorkflowDelegate<WY,USER>,
					FRC extends JeeslFileContainer<?,?>,
					USER extends JeeslUser<SR>>

{
	final static Logger logger = LoggerFactory.getLogger(WorkflowEscalationHandler.class);
	
	private final JeeslWorkflowFacade<L,D,LOC,WX,WP,WS,WST,WSP,WPT,WML,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fWorkflow;
	private final WorkflowFactoryBuilder<L,D,WX,WP,WS,WST,WSP,WPT,WML,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow;

	private final JeeslWorkflowCommunicator<L,D,LOC,WX,WP,WS,WST,WSP,WPT,WML,WT,WTT,WC,WA,AB,AO,MT,MC,MD,SR,RE,RA,WF,WY,FRC,USER> communicator;
	
	private final WTT typeEscalation;
	private final USER user;
	
	public WorkflowEscalationHandler(JeeslWorkflowFacade<L,D,LOC,WX,WP,WS,WST,WSP,WPT,WML,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fWorkflow,
									WorkflowFactoryBuilder<L,D,WX,WP,WS,WST,WSP,WPT,WML,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow,
									JeeslWorkflowMessageHandler<WC,SR,RE,MT,MC,MD,WF,WY,USER> messageHandler,
									USER user)
	{
		this.fWorkflow=fWorkflow;
		this.fbWorkflow=fbWorkflow;
		this.user=user;
		
		communicator = new JeeslWorkflowCommunicator<>(messageHandler);
		communicator.setDebugOnInfo(true);
		
		typeEscalation = fWorkflow.fByEnum(fbWorkflow.getClassTransitionType(),JeeslWorkflowTransitionType.Code.escalation);
	}
	
	public void escalte(WP process)
	{
		DateTime now = new DateTime();
		List<WL> list = fWorkflow.fWorkflowsEscalation(process);
		logger.info(Workflow.class.getSimpleName()+": "+list.size());
		for(WL link : list)
		{
			for(WT t : fWorkflow.allForParent(fbWorkflow.getClassTransition(), link.getWorkflow().getCurrentStage()))
			{
				if(t.getType().equals(typeEscalation) && t.getAutoTransitionTimer()!=null)
				{
					DateTime dt = new DateTime(link.getWorkflow().getLastActivity().getRecord());
					boolean timeOver = now.isAfter(dt.plusHours(t.getAutoTransitionTimer()));
					logger.info(t.toString()+" perform: "+timeOver);
					if(timeOver)
					{
						try
						{
							escalate(link,t);
						}
						catch (JeeslConstraintViolationException | JeeslLockingException | ClassNotFoundException | JeeslNotFoundException e) {e.printStackTrace();}
					}
				}
				
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void escalate(WL link, WT transition) throws JeeslConstraintViolationException, JeeslLockingException, ClassNotFoundException, JeeslNotFoundException
	{
		WF workflow = link.getWorkflow();
		logger.info("Perform ");
		List<WC> communications = fWorkflow.allForParent(fbWorkflow.getClassCommunication(),transition);
		
		Class<JeeslWithWorkflow<WF>> c = (Class<JeeslWithWorkflow<WF>>)Class.forName(link.getEntity().getCode()).asSubclass(JeeslWithWorkflow.class);
		JeeslWithWorkflow<WF> entity = fWorkflow.find(c,link.getRefId());
		
		workflow.setCurrentStage(transition.getDestination());
		workflow = fWorkflow.save(workflow);
		
		WY activity = fbWorkflow.ejbActivity().build(workflow,transition,user);
		activity.setRemark("");
		activity = fWorkflow.save(activity);
		
		workflow.setLastActivity(activity);
		workflow = fWorkflow.save(workflow);
		communicator.build(activity,entity,communications);
	}
}