package org.jeesl.controller.handler.module.workflow;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.exception.JeeslWorkflowException;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.ejb.util.EjbCodeFactory;
import org.jeesl.factory.ejb.util.EjbIdFactory;
import org.jeesl.factory.png.SignatureTranscoder;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowActionsHandler;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowMessageHandler;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowResponsibleHandler;
import org.jeesl.interfaces.controller.handler.system.io.JeeslFileRepositoryHandler;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplateDefinition;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
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
import org.jeesl.interfaces.model.system.constraint.JeeslConstraint;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.web.JeeslJsfSecurityHandler;
import org.jeesl.interfaces.web.JeeslJsfWorkflowHandler;
import org.jeesl.util.comparator.ejb.RecordComparator;
import org.jeesl.util.comparator.pojo.BooleanComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslWorkflowEngine <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslStatus<L,D,LOC>,
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
									WC extends JeeslWorkflowActionNotification<WT,MT,MC,SR,RE>,
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
									WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
									WD extends JeeslWorkflowDelegate<WY,USER>,
									FRC extends JeeslFileContainer<?,?>,
									WCS extends JeeslConstraint<L,D,?,?,?,?,?,?>,
									USER extends JeeslUser<SR>
									>
				implements JeeslJsfWorkflowHandler
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(JeeslWorkflowEngine.class);

	private boolean debugOnInfo; public void setDebugOnInfo(boolean debugOnInfo){this.debugOnInfo=debugOnInfo;if(actionHandler!=null) {actionHandler.setDebugOnInfo(debugOnInfo);}}
	private boolean allowTransitions; public boolean isAllowTransitions() {return allowTransitions;} public void setAllowTransitions(boolean allowTransitions) {this.allowTransitions = allowTransitions;}

	private final JeeslWorkflowFacade<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fWorkflow;

	private final WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?,?> fbRevision;

	private JeeslJsfSecurityHandler<SR,?,?,?,?,USER> security;
	private JeeslFileRepositoryHandler<?,FRC,?> frh; public JeeslFileRepositoryHandler<?,FRC,?> getFrh() {return frh;}
	private final JeeslWorkflowCommunicator<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,WC,WA,AB,AO,MT,MC,MD,SR,RE,RA,WL,WF,WY,FRC,USER> communicator;
	private final JeeslWorkflowActionsHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WCS,USER> actionHandler;
	private final JeeslWorkflowResponsibleHandler<WF,USER> responsibleHandler;

	private final Comparator<WY> cpActivity;

	private final Map<JeeslWithWorkflow<WF>,WF> mapWorkflow; public Map<JeeslWithWorkflow<WF>,WF> getMapWorkflow() {return mapWorkflow;}
	private final Map<WY,byte[]> mapSignature; public Map<WY, byte[]> getMapSignature() {return mapSignature;}
	private final Map<WT,Boolean> mapVeto; public Map<WT,Boolean> getMapVeto() {return mapVeto;}
	private final Map<String,WPD> mapDocument; public Map<String,WPD> getMapDocument() {return mapDocument;}

	private final List<WY> activities; public List<WY> getActivities() {return activities;}
	private final List<WT> transitions; public List<WT> getTransitions() {return transitions;}
	private final List<WA> actions; public List<WA> getActions() {return actions;}
	private final List<WC> communications; public List<WC> getCommunications() {return communications;}
	private final List<WPD> documents; public List<WPD> getDocuments() {return documents;}
	private final List<WCS> constraints; public List<WCS> getConstraints() {return constraints;}
	private final List<WML> levels; public List<WML> getLevels() {return levels;}

	private USER user;
	private JeeslWithWorkflow<WF> entity;

	protected WP process; public WP getProcess() {return process;} protected void setProcess(WP process) {this.process = process;}
	private final WPT typeDelegate; public WPT getTypeDelegate() {return typeDelegate;}
	private WL link; public WL getLink() {return link;} public void setLink(WL link) {this.link = link;}
	private WF workflow; public WF getWorkflow() {return workflow;} public void setWorkflow(WF workflow) {this.workflow = workflow;}
	private WY activity; public WY getActivity() {return activity;} public void setActivity(WY activity) {this.activity = activity;}
	private WT transition; public WT getTransition() {return transition;}
	private WD delegate; public WD getDelegate() {return delegate;} public void setDelegate(WD delegate) {this.delegate = delegate;}
	private String remark; public String getRemark() {return remark;} public void setRemark(String remark) {this.remark = remark;}
	private String screenSignature; public String getScreenSignature() {return screenSignature;}public void setScreenSignature(String screenSignature) {this.screenSignature = screenSignature;}

	private boolean historyWithSignature; public boolean isHistoryWithSignature() {return historyWithSignature;}
	private boolean allowEntityModifications; @Override public boolean isAllowEntityModifications() {return allowEntityModifications;}
	private boolean allowAdminModifications; @Override public boolean isAllowAdminModifications() {return allowAdminModifications;}
	private boolean delegationRoles; public boolean isDelegationRoles() {return delegationRoles;}

	@Override public boolean isAllowModifications() {return allowEntityModifications||allowAdminModifications;}

	public JeeslWorkflowEngine(WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow,
								IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?,?> fbRevision,
								JeeslWorkflowFacade<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WSN,WT,WTT,WC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fWorkflow,
								JeeslWorkflowMessageHandler<WS,WC,SR,RE,MT,MC,MD,WF,WY,USER> messageHandler,
								JeeslWorkflowActionsHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WCS,USER> actionHandler,
								JeeslWorkflowResponsibleHandler<WF,USER> responsibleHandler,
								JeeslFileRepositoryHandler<?,FRC,?> frh)
	{
		this.fbWorkflow=fbWorkflow;
		this.fbRevision=fbRevision;

		this.fWorkflow=fWorkflow;
		this.responsibleHandler=responsibleHandler;
		this.frh=frh;

		debugOnInfo = false;
		allowTransitions = true;

		cpActivity = new RecordComparator<WY>();

		mapDocument = new HashMap<>();
		mapWorkflow = new HashMap<>();
		mapSignature = new HashMap<>();
		mapVeto = new HashMap<>();

		transitions = new ArrayList<>();
		activities = new ArrayList<>();
		actions = new ArrayList<>();
		communications = new ArrayList<>();
		documents = new ArrayList<>();
		constraints = new ArrayList<>();
		levels = new ArrayList<>();

		communicator = new JeeslWorkflowCommunicator<>(messageHandler);
		communicator.setDebugOnInfo(debugOnInfo);

		this.actionHandler=actionHandler;
		if(this.actionHandler!=null) {this.actionHandler.setDebugOnInfo(debugOnInfo);}

		typeDelegate = fWorkflow.fByEnum(fbWorkflow.getClassPermissionType(),JeeslWorkflowPermissionType.Code.delegate);
	}

	public void reset() {reset(true,true,true,true,true,true);}
	public void clearSignature(){reset(false,false,true,false,false,false);}
	private void reset(boolean rTransitions, boolean rTransition, boolean rSignature, boolean rWorkflow, boolean rDelegate, boolean rFrh)
	{
		if(rTransitions) {transitions.clear();}
		if(rTransition) {transition = null;}
		if(rSignature) {screenSignature = null;}
		if(rWorkflow) {workflow=null;link=null;activities.clear();}
		if(rDelegate) {delegate=null;}
//		if(rFrh) {frh.reset();}
	}

	protected void realodDocuments()
	{
		List<WPD> documents = fWorkflow.allForParent(fbWorkflow.getClassDocument(),process);
		mapDocument.putAll(EjbCodeFactory.toMapNonUniqueCode(documents));
		logger.info("Documents: "+documents.size());
	}

	public void addWorkflow(JeeslJsfSecurityHandler<SR,?,?,?,?,USER> security, USER user, JeeslWithWorkflow<WF> ejb)
	{
		this.security = security;
		this.user=user;
		this.entity = ejb;
		reset(true,true,true,true,true,true);
		workflow = fbWorkflow.ejbWorkflow().build(process);

		WT transition = fWorkflow.fTransitionBegin(process);
		workflow.setCurrentStage(transition.getDestination());
		if(debugOnInfo) {logger.info("Using transition: "+transition.toString());}

		WY activity = fbWorkflow.ejbActivity().build(workflow,transition,user);
		workflow.getActivities().add(activity);
		workflow.setLastActivity(activity);

		RE entity = null;
		try {entity = fWorkflow.fByCode(fbRevision.getClassEntity(),ejb.getClass().getName());}
		catch (JeeslNotFoundException e) {e.printStackTrace();}

		link = fbWorkflow.ejbLink().build(entity,workflow,ejb);
		if(debugOnInfo) {logger.info("Build: Workflow and Link");}
		reloadWorkflow(false);
	}

	public <W extends JeeslWithWorkflow<WF>> void saveWorkflow(JeeslWithWorkflow<WF> ejb) throws JeeslConstraintViolationException, JeeslLockingException
	{
		this.entity=ejb;
		if(workflow!=null)
		{
			workflow = fWorkflow.save(workflow);
			link.setWorkflow(workflow);
			link.setRefId(ejb.getId());
			link = fWorkflow.save(link);

			if(debugOnInfo) {logger.info("Saved: Workflow and Link ("+workflow.toString()+" , "+link.toString()+")");}
		}
	}

	public <W extends JeeslWithWorkflow<WF>> void selectEntity(JeeslJsfSecurityHandler<SR,?,?,?,?,USER> security, USER user, W ejb) throws JeeslNotFoundException
	{
		this.security=security;
		this.user=user;
		this.entity = ejb;
		reset(true,true,true,true,true,true);

		link = fWorkflow.fWorkflowLink(process,ejb);
		workflow = link.getWorkflow();
		if(debugOnInfo) {logger.info("Select: Workflow and Link");}
		reloadWorkflow(false);
	}

	public <W extends JeeslWithWorkflow<WF>> void loadWorkflows(List<W> ejbs)
	{
		mapWorkflow.clear();
		for(W  w : ejbs)
		{
			try
			{
				WL link = fWorkflow.fWorkflowLink(process,w);
				mapWorkflow.put(w,link.getWorkflow());
			}
			catch (JeeslNotFoundException e) {}
		}
	}

	public void reloadWorkflow() {reloadWorkflow(true);}
	public void reloadWorkflow(boolean ejbLoadWorkflow)
	{
		reset(true,true,true,false,true,true);
		if(workflow==null) {return;}
		if(ejbLoadWorkflow) {workflow = fWorkflow.find(fbWorkflow.getClassWorkflow(),workflow);}

		mapWorkflow.put(entity,workflow);

		constraints.clear();

		List<WSP> availablePermissions = fWorkflow.allForParent(fbWorkflow.getClassPermission(), workflow.getCurrentStage());
		if(debugOnInfo) {logger.info("Checking "+availablePermissions.size()+" "+fbWorkflow.getClassPermission().getSimpleName());}

		levels.clear();
		boolean hasResponsibleRole = false;
		allowEntityModifications = false;
		allowAdminModifications = false;
		delegationRoles = false;
		for(WSP wsp : availablePermissions)
		{
			boolean wspIsResponsible = wsp.getType().getCode().contentEquals(JeeslWorkflowPermissionType.Code.responsible.toString());
			boolean wspIsDelegation = wsp.getType().getCode().contentEquals(JeeslWorkflowPermissionType.Code.delegate.toString());
			boolean userHasRole = security.hasRole(wsp.getRole());
			boolean wspIsFullAllow = wsp.getModificationLevel().getCode().contentEquals(JeeslWorkflowModificationLevel.Code.full.toString());
			boolean wspIsAdminAllow = wsp.getModificationLevel().getCode().contentEquals(JeeslWorkflowModificationLevel.Code.admin.toString());

			if(wspIsResponsible && userHasRole)
			{
				if(!levels.contains(wsp.getModificationLevel())) {levels.add(wsp.getModificationLevel());}
				hasResponsibleRole=true;
			}
			if(wspIsDelegation && userHasRole)
			{
				delegationRoles = true;
			}
			if(wspIsFullAllow && userHasRole)
			{
				if(!levels.contains(wsp.getModificationLevel())) {levels.add(wsp.getModificationLevel());}
				allowEntityModifications=true;
			}
			if(wspIsAdminAllow && userHasRole) {allowAdminModifications=true;}
			if(debugOnInfo) {logger.info("\t"+wsp.getPosition()+" "+wsp.getRole().getCode()+":"+userHasRole+" "+JeeslWorkflowPermissionType.Code.responsible+":"+wspIsResponsible+" "+JeeslWorkflowModificationLevel.Code.full+":"+wspIsFullAllow+" "+JeeslWorkflowModificationLevel.Code.admin+":"+allowAdminModifications);}
		}

		boolean hasDelegate = false;
		if(workflow.getLastActivity()!=null && workflow.getLastActivity().getDelegate()!=null)
		{
			boolean isApproved = BooleanComparator.active(workflow.getLastActivity().getDelegate().getResult());
			boolean isUser = user.equals(workflow.getLastActivity().getDelegate().getUserRequest());
			hasDelegate = isApproved && isUser;
		}

		if(EjbIdFactory.isSaved(entity))
		{
			List<WT> availableTransitions = fWorkflow.allForParent(fbWorkflow.getClassTransition(), workflow.getCurrentStage());
			if(debugOnInfo) {logger.info("Checking "+availableTransitions.size()+" "+fbWorkflow.getClassTransition().getSimpleName());}
			mapVeto.clear();
			for(WT t : availableTransitions)
			{
				StringBuilder sb = null;
				if(debugOnInfo){sb = new StringBuilder();sb.append("\tChecking "+fbWorkflow.getClassTransition().getSimpleName()+" "+t.getPosition()+" visible:"+t.isVisible());}
				if(t.isVisible() && t.getType().getCode().equals(JeeslWorkflowTransitionType.Code.user.toString()))
				{
					if(t.getRole()==null)
					{
						if(debugOnInfo) {sb.append(" has no special role, adding if responsible?"+hasResponsibleRole);}
						if(hasResponsibleRole || hasDelegate) {transitions.add(t);}
					}
					else
					{
						boolean hasSpecialRole = security.hasRole(t.getRole());
						if(debugOnInfo) {sb.append(" has special role ").append(t.getRole().getCode()).append(" user:").append(hasSpecialRole);}
						if(hasSpecialRole) {transitions.add(t);}
					}
				}
				boolean veto = actionHandler.checkVeto(entity,t);
				mapVeto.put(t,veto);
				if(debugOnInfo) {sb.append(" veto:").append(veto);}
				if(debugOnInfo) {logger.info(sb.toString());}
			}
		}
		else if(debugOnInfo) {logger.info("Not Checking Transitions because etiher hasResponsibleRole:"+hasResponsibleRole+" or isSaved"+EjbIdFactory.isSaved(entity));}

		reloadActivities();

		if(workflow!=null && workflow.getLastActivity()!=null && workflow.getLastActivity().getDelegate()!=null)
		{
			delegate = workflow.getLastActivity().getDelegate();
		}

		if(debugOnInfo) {logger.info("reloadWorkflow: "+transitions.size()+" "+fbWorkflow.getClassTransition().getSimpleName());}
	}

	private void reloadActivities()
	{
		activities.clear();
		if(EjbIdFactory.isSaved(workflow)){activities.addAll(fWorkflow.allForParent(fbWorkflow.getClassActivity(), workflow));}

		Collections.sort(activities,cpActivity);
		Collections.reverse(activities);

		mapSignature.clear();
		for(WY a : activities)
		{
			if(a.getScreenSignature()!=null)
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try
				{
					SignatureTranscoder.generateSignature(a.getScreenSignature(),os,4);
					mapSignature.put(a,os.toByteArray());
				}
				catch (IOException e) {e.printStackTrace();}
			}
		}
		historyWithSignature = !mapSignature.isEmpty();
	}

	public void requestDelegate()
	{
		if(debugOnInfo) {logger.info("Request Delegate ");}
		if(delegate==null)
		{
			delegate = fbWorkflow.ejbDelegate().build(workflow.getLastActivity(), user);
		}
	}

	public void saveDelegate() throws JeeslConstraintViolationException, JeeslLockingException
	{
		delegate = fWorkflow.save(delegate);
		reloadActivities();
	}

	public void prepareTransition(WT t, boolean autoPerform) throws JeeslConstraintViolationException, JeeslLockingException, UtilsProcessingException, JeeslWorkflowException, JeeslNotFoundException
	{
		transition = fWorkflow.loadTransition(t);
		if(debugOnInfo) {logger.info("Prepare Transition for "+transition.toString()+" using "+actionHandler.getClass().getName());}

		remark="";
		screenSignature=null;

		actions.clear();actions.addAll(fWorkflow.allForParent(fbWorkflow.getClassAction(),transition));
		communications.clear();communications.addAll(fWorkflow.allForParent(fbWorkflow.getClassCommunication(),transition));
		documents.clear();documents.addAll(transition.getDocuments());

		constraints.clear();
		actionHandler.checkPreconditions(constraints,entity,actions);
		if(!documents.isEmpty()) {actionHandler.checkDocuments(constraints,entity,documents);}

		if(debugOnInfo)
		{
			StringBuilder sb = new StringBuilder();
			sb.append("Prepared "+fbWorkflow.getClassTransition().getSimpleName());
			sb.append(" with t=").append(transition.getId());
			sb.append(" to "+transition.getDestination().getCode());
			sb.append(" communications:").append(communications.size());
			sb.append(" actions:").append(actions.size());
			logger.info(sb.toString());
		}
		if(autoPerform && constraints.isEmpty()) {performTransition();}
	}

	public void performTransition() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException, UtilsProcessingException
	{
		if(debugOnInfo) {logger.info("Perform "+fbWorkflow.getClassTransition().getSimpleName()+" to "+transition.getDestination().getCode());}

		constraints.clear();
		actionHandler.checkPreconditions(constraints,entity,actions);
		actionHandler.checkRemark(constraints,transition,remark);

		if(!constraints.isEmpty())
		{
			if(debugOnInfo) {logger.info("PreconditionCheck failed. Aborting.");}
			return;
		}

		try
		{
			entity = actionHandler.perform(user,transition,entity,actions);

			workflow.setCurrentStage(transition.getDestination());
			workflow = fWorkflow.save(workflow);

			activity = fbWorkflow.ejbActivity().build(workflow,transition,user);
			activity.setRemark(remark);
			activity.setScreenSignature(screenSignature);
			activity = fWorkflow.save(activity);

			workflow.setLastActivity(activity);
			workflow = fWorkflow.save(workflow);

			workflow = fWorkflow.loadWorkflow(workflow);
			workflow.getResponsibles().clear();
			workflow = fWorkflow.save(workflow);
			if(responsibleHandler!=null)
			{
				workflow.setResponsibles(responsibleHandler.findResponsibles(workflow, entity));
				if(debugOnInfo) {logger.info("Responsible Users: "+workflow.getResponsibles().size());}
				workflow = fWorkflow.save(workflow);
			}

			communicator.build(activity,entity,communications);

			remark = null;
			screenSignature = null;
			transition=null;
			activity = null;

			reloadWorkflow(true);
		}
		catch (JeeslWorkflowException e)
		{
			logger.warn(e.getMessage());
			actionHandler.abort(entity);
		}
	}
}