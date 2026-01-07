package org.jeesl.api.facade.module;

import java.util.Date;
import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.facade.JeeslFacade;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowDelegate;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStageType;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransitionType;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.util.query.module.JeeslWorkflowQuery;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples1;
import org.jeesl.model.json.io.db.tuple.container.JsonTuples2;

public interface JeeslWorkflowFacade <
										WP extends JeeslWorkflowProcess<?,?,?,WS>,
										
										WS extends JeeslWorkflowStage<?,?,WP,WST,?,WT,?>,
										WST extends JeeslWorkflowStageType<?,?,WST,?>,
										WSN extends JeeslWorkflowStageNotification<WS,?,?,SR,?>,
										WT extends JeeslWorkflowTransition<?,?,?,WS,WTT,SR,?>,
										WTT extends JeeslWorkflowTransitionType<?,?,WTT,?>,
										

										SR extends JeeslSecurityRole<?,?,?,?,?,?>,

										WL extends JeeslWorkflowLink<WF,?>,
										WF extends JeeslWorkflow<WP,WS,WY,USER>,
										WY extends JeeslWorkflowActivity<WT,WF,WD,?,USER>,
										WD extends JeeslWorkflowDelegate<WY,USER>,
										
										USER extends JeeslUser<SR>>
			extends JeeslFacade
{	
	WT fTransitionBegin(WP process);
	
	WL fWorkflowLink(WF workflow) throws JeeslNotFoundException;
	<W extends JeeslWithWorkflow<WF>> WL fWorkflowLink(WP process, W owner) throws JeeslNotFoundException;
	
	List<WL> fWorkflowLinks(JeeslWorkflowQuery<WP,WF> query);
	<W extends JeeslWithWorkflow<WF>> List<WL> fWorkflowLinks(WP process, List<W> owners);
	List<WL> fWorkflowRepsonsibleLinks(USER user);
	List<WL> fWorkflowDelegationLinks(USER user);
	List<WL> fWorkflowEscalations(WP process);
	List<WL> fWorkflowDelegationReuquests(Boolean result);
	List<WL> fWorkflowsForNotification(WSN notifciation);
	
	WT loadTransition(WT transition);
	
	WF loadWorkflow(WF workflow);
	void deleteWorkflow(WL link) throws JeeslConstraintViolationException;
	
	List<WF> fWorkflows(JeeslWorkflowQuery<WP,WF> query);
	List<WF> fWorkflows(WP process, List<WS> stages);
	List<WF> fWorkflows(List<WP> processes, List<WST> types);
	
	JsonTuples1<WP> tpcActivitiesByProcess();
	JsonTuples2<WP,WST> tpcActivitiesByProcessType();
	
	List<WY> fWorkflowActivities(Date from, Date to, List<USER> users, List<WP> processes);
}