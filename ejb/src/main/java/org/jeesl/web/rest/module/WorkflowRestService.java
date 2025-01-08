package org.jeesl.web.rest.module;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.api.rest.rs.module.workflow.JeeslWorkflowRestExportInterface;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.xml.io.locale.status.XmlContextsFactory;
import org.jeesl.factory.xml.module.workflow.XmlProcessFactory;
import org.jeesl.factory.xml.module.workflow.XmlProcessesFactory;
import org.jeesl.factory.xml.module.workflow.XmlWorkflowFactory;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
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
import org.jeesl.model.xml.module.workflow.Processes;
import org.jeesl.model.xml.module.workflow.Workflow;
import org.jeesl.model.xml.xsd.Container;
import org.jeesl.util.query.xml.module.XmlWorkflowQuery;
import org.jeesl.web.rest.AbstractJeeslRestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowRestService <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
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
									SR extends JeeslSecurityRole<L,D,?,?,?,?>,
									RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
									RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
									AL extends JeeslWorkflowLink<WF,RE>,
									WF extends JeeslWorkflow<WP,WS,WY,USER>,
									WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
									WD extends JeeslWorkflowDelegate<WY,USER>,
									FRC extends JeeslFileContainer<?,?>,
									USER extends JeeslUser<SR>>
					extends AbstractJeeslRestHandler<L,D>
					implements JeeslWorkflowRestExportInterface
{
	final static Logger logger = LoggerFactory.getLogger(WorkflowRestService.class);
	
	private final JeeslWorkflowFacade<WP,WS,WST,WSN,WT,WTT,SR,AL,WF,WY,WD,USER> fWorkflow;
	private final WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,AL,WF,WY,WD,FRC,USER> fbWorkflow;
	
	private final XmlProcessFactory<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,SR> xfProcess;
	
	private WorkflowRestService(WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,AL,WF,WY,WD,FRC,USER> fbWorkflow,
			JeeslWorkflowFacade<WP,WS,WST,WSN,WT,WTT,SR,AL,WF,WY,WD,USER> fWorkflow)
	{
		super(fWorkflow,fbWorkflow.getClassL(),fbWorkflow.getClassD());
		this.fWorkflow=fWorkflow;
		this.fbWorkflow=fbWorkflow;
		
		xfProcess = new XmlProcessFactory<>(XmlWorkflowQuery.get(XmlWorkflowQuery.Key.xProcess));
		xfProcess.lazy(fbWorkflow, fWorkflow);
	}
	
	public static <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
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
						SR extends JeeslSecurityRole<L,D,?,?,?,?>,
						RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
						RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
						AL extends JeeslWorkflowLink<WF,RE>,
						WF extends JeeslWorkflow<WP,WS,WY,USER>,
						WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
						WD extends JeeslWorkflowDelegate<WY,USER>,
						FRC extends JeeslFileContainer<?,?>,
						USER extends JeeslUser<SR>>
			WorkflowRestService<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,AL,WF,WY,WD,FRC,USER>
			factory(WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,AL,WF,WY,WD,FRC,USER> fbWorkflow,
					JeeslWorkflowFacade<WP,WS,WST,WSN,WT,WTT,SR,AL,WF,WY,WD,USER> fWorkflow)
	{
		return new WorkflowRestService<>(fbWorkflow,fWorkflow);
	}

	@Override public Container exportWorkflowContext() {return xfContainer.build(fWorkflow.allOrderedPosition(fbWorkflow.getClassContext()));}

	@Override
	public Workflow exportWorkflowProcesses()
	{
		Workflow xml = XmlWorkflowFactory.build();
		xml.setContexts(XmlContextsFactory.build(exportWorkflowContext().getStatus()));
		
		Processes processes = XmlProcessesFactory.build();
		for(WP process : fWorkflow.all(fbWorkflow.getClassProcess()))
		{
			processes.getProcess().add(xfProcess.build(process));
		}
		xml.setProcesses(processes);
		
		return xml;
	}
}