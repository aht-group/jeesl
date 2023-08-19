package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowDelegate;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowActionNotification;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowDocument;
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
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslWorkflowFacade <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
										WX extends JeeslWorkflowContext<L,D,WX,?>,
										WP extends JeeslWorkflowProcess<L,D,WX,WS>,
										WPD extends JeeslWorkflowDocument<L,D,WP>,
										WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
										WST extends JeeslWorkflowStageType<L,D,WST,?>,
										WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
										WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
										WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
										WSN extends JeeslWorkflowStageNotification<WS,MT,MC,SR,RE>,
										WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,SR,?>,
										WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
										AC extends JeeslWorkflowActionNotification<WT,MT,MC,SR,RE>,
										WA extends JeeslWorkflowAction<WT,AB,AO,RE,RA>,
										AB extends JeeslWorkflowBot<AB,L,D,?>,
										AO extends EjbWithId,
										MT extends JeeslIoTemplate<L,D,?,?,?,?>,
										MC extends JeeslTemplateChannel<L,D,MC,?>,
										SR extends JeeslSecurityRole<L,D,?,?,?,?,?>,
										RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
										WL extends JeeslWorkflowLink<WF,RE>,
										WF extends JeeslWorkflow<WP,WS,WY,USER>,
										WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
										WD extends JeeslWorkflowDelegate<WY,USER>,
										FRC extends JeeslFileContainer<?,?>,
										USER extends JeeslUser<SR>>
			extends JeeslFacade
{	
	WT fTransitionBegin(WP process);
	
	WL fWorkflowLink(WF workflow) throws JeeslNotFoundException;
	<W extends JeeslWithWorkflow<WF>> WL fWorkflowLink(WP process, W owner) throws JeeslNotFoundException;
	List<WL> fWorkflowLinks(List<WF> workflows);
	<W extends JeeslWithWorkflow<WF>> List<WL> fWorkflowLinks(WP process, List<W> owners);
	List<WL> fWorkflowRepsonsibleLinks(USER user);
	List<WL> fWorkflowDelegationLinks(USER user);
	List<WL> fWorkflowEscalations(WP process);
	List<WL> fWorkflowDelegationReuquests(Boolean result);
	List<WL> fWorkflowsForNotification(WSN notifciation);
	
	WT loadTransition(WT transition);
	
	WF loadWorkflow(WF workflow);
	List<WF> fWorkflows(WP process, List<WS> stages);
	List<WF> fWorkflows(List<WP> processes, List<WST> types);
	
	JsonTuples1<WP> tpcActivitiesByProcess();
	JsonTuples2<WP,WST> tpcActivitiesByProcessType();
	
	List<WY> fWorkflowActivities(Date from, Date to, List<USER> users, List<WP> processes);
}