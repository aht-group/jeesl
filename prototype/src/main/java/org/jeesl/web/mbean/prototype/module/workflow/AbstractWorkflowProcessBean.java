package org.jeesl.web.mbean.prototype.module.workflow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jeesl.api.bean.JeeslTranslationBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.api.facade.io.JeeslIoRevisionFacade;
import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.api.facade.system.graphic.JeeslGraphicFacade;
import org.jeesl.controller.handler.NullNumberBinder;
import org.jeesl.controller.handler.module.workflow.WorkflowProcesslResetHandler;
import org.jeesl.controller.handler.op.OpEntitySelectionHandler;
import org.jeesl.controller.handler.sb.SbSingleHandler;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.factory.builder.io.IoRevisionFactoryBuilder;
import org.jeesl.factory.builder.io.IoTemplateFactoryBuilder;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.builder.system.SecurityFactoryBuilder;
import org.jeesl.factory.builder.system.SvgFactoryBuilder;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowTransitionFactory;
import org.jeesl.factory.json.system.translation.JsonTranslationFactory;
import org.jeesl.interfaces.bean.op.OpEntityBean;
import org.jeesl.interfaces.bean.sb.SbSingleBean;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowCommunication;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowDelegate;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowLink;
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
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphicType;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.JeeslLocale;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.jsf.handler.PositionListReorderer;
import org.jeesl.util.comparator.ejb.system.security.SecurityRoleComparator;
import org.jeesl.web.mbean.prototype.system.AbstractAdminBean;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.web.mbean.util.AbstractLogMessage;

public abstract class AbstractWorkflowProcessBean <L extends JeeslLang, D extends JeeslDescription, LOC extends JeeslLocale<L,D,LOC,?>,
											WX extends JeeslWorkflowContext<L,D,WX,G>,
											WP extends JeeslWorkflowProcess<L,D,WX,WS>,
											WPD extends JeeslWorkflowDocument<L,D,WP>,
											WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,G>,
											WST extends JeeslWorkflowStageType<L,D,WST,?>,
											WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
											WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
											WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
											WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,SR,G>,
											WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
											AC extends JeeslWorkflowCommunication<WT,MT,MC,SR,RE>,
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
											G extends JeeslGraphic<L,D,GT,?,?>, GT extends JeeslGraphicType<L,D,GT,G>,
											USER extends JeeslUser<SR>>
					extends AbstractAdminBean<L,D,LOC>
					implements Serializable,SbSingleBean,OpEntityBean
{
	private static final long serialVersionUID = 1L;
	final static Logger logger = LoggerFactory.getLogger(AbstractWorkflowProcessBean.class);

	private JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic;
	private JeeslWorkflowFacade<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fWorkflow;
	private JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fRevision;
	
	private final WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbWorkflow;
	private final IoTemplateFactoryBuilder<L,D,?,MC,MT,?,?,?,?> fbTemplate;
	private final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fbRevision;
	private final SecurityFactoryBuilder<L,D,?,SR,?,?,?,?,?,?,?,?> fbSecurity;
	private final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg;
	
	private final EjbWorkflowTransitionFactory<WS,WT> efTransition;
	
	private final SbSingleHandler<WX> sbhContext; public SbSingleHandler<WX> getSbhContext() {return sbhContext;}
	private final SbSingleHandler<WP> sbhProcess; public SbSingleHandler<WP> getSbhProcess() {return sbhProcess;}
	private final OpEntitySelectionHandler<WPD> opDocument; public OpEntitySelectionHandler<WPD> getOpDocument() {return opDocument;}
	
	private final NullNumberBinder nnb; public NullNumberBinder getNnb() {return nnb;}
	
	private final List<MC> channels; public List<MC> getChannels() {return channels;}
	protected final List<MT> templates; public List<MT> getTemplates() {return templates;}
	private final List<SR> roles; public List<SR> getRoles() {return roles;}
	private final List<WPD> documents; public List<WPD> getDocuments() {return documents;}
	private final List<WS> stages; public List<WS> getStages() {return stages;}
	private final List<WST> stageTypes; public List<WST> getStageTypes() {return stageTypes;}
	private final List<WSP> permissions; public List<WSP> getPermissions() {return permissions;}
	private final List<WPT> permissionTypes; public List<WPT> getPermissionTypes() {return permissionTypes;}
	private final List<WML> modificationLevels; public List<WML> getModificationLevels() {return modificationLevels;}
	private final List<WT> transitions; public List<WT> getTransitions() {return transitions;}
	private final List<WTT> transitionTypes; public List<WTT> getTransitionTypes() {return transitionTypes;}
	private final List<AC> communications; public List<AC> getCommunications() {return communications;}
	private final List<WA> actions; public List<WA> getActions() {return actions;}
	private final List<AB> bots; public List<AB> getBots() {return bots;}
	protected final List<RE> entities; public List<RE> getEntities() {return entities;}
	protected final List<RE> scopes; public List<RE> getScopes() {return scopes;}
	private final List<RA> attributes; public List<RA> getAttributes() {return attributes;}
	private final List<EjbWithId> options; public List<EjbWithId> getOptions() {return options;}
	
	protected WP process; public WP getProcess() {return process;} public void setProcess(WP process) {this.process = process;}
	private WPD document; public WPD getDocument() {return document;} public void setDocument(WPD document) {this.document = document;}
	private WS stage; public WS getStage() {return stage;} public void setStage(WS stage) {this.stage = stage;}
	private WSP permission; public WSP getPermission() {return permission;} public void setPermission(WSP permission) {this.permission = permission;}
	private WT transition; public WT getTransition() {return transition;} public void setTransition(WT transition) {this.transition = transition;}
	private AC communication; public AC getCommunication() {return communication;} public void setCommunication(AC communication) {this.communication = communication;}
	private WA action; public WA getAction() {return action;} public void setAction(WA action) {this.action = action;}
	
	private Class<EjbWithPosition> cOption;
	private Long option; public Long getOption() {return option;} public void setOption(Long option) {this.option = option;}
	
	private boolean editStage; public boolean isEditStage() {return editStage;} public void toggleEditStage() {editStage=!editStage;reloadStageSelectOne();}
	private boolean editTransition; public boolean isEditTransition() {return editTransition;} public void toggleEditTransition() {editTransition=!editTransition;}

	private final Comparator<SR> cpRole;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AbstractWorkflowProcessBean(final WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fbApproval,
											final IoRevisionFactoryBuilder<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fbRevision,
											final SecurityFactoryBuilder<L,D,?,SR,?,?,?,?,?,?,?,?> fbSecurity,
											final IoTemplateFactoryBuilder<L,D,?,MC,MT,?,?,?,?> fbTemplate,
											final SvgFactoryBuilder<L,D,G,GT,?,?> fbSvg)
	{
		super(fbApproval.getClassL(),fbApproval.getClassD());
		this.fbWorkflow=fbApproval;
		this.fbRevision=fbRevision;
		this.fbSecurity=fbSecurity;
		this.fbTemplate=fbTemplate;
		this.fbSvg=fbSvg;
		
		opDocument = new OpEntitySelectionHandler<WPD>(this);
		opDocument.addColumn(JsonTranslationFactory.build(fbWorkflow.getClassDocument(),JeeslWorkflowDocument.Attributes.code,"@code","var.code"));
		
		efTransition = fbWorkflow.ejbTransition();
		
		sbhContext = new SbSingleHandler<WX>(fbApproval.getClassContext(),this);
		sbhProcess = new SbSingleHandler<WP>(fbApproval.getClassProcess(),this);
		
		nnb = new NullNumberBinder();
		
		channels = new ArrayList<>();
		roles = new ArrayList<>();
		documents = new ArrayList<>();
		stages = new ArrayList<>();
		templates = new ArrayList<>();
		stageTypes = new ArrayList<>();
		permissions = new ArrayList<>();
		permissionTypes = new ArrayList<>();
		modificationLevels = new ArrayList<>();
		transitions = new ArrayList<>();
		transitionTypes = new ArrayList<>();
		communications = new ArrayList<>();
		actions = new ArrayList<>();
		bots = new ArrayList<>();
		entities = new ArrayList<>();
		scopes = new ArrayList<>();
		attributes = new ArrayList<>();
		options = new ArrayList<>();
		
		editStage = false;
		editTransition = false;
		
		cpRole = (new SecurityRoleComparator()).factory(SecurityRoleComparator.Type.position);
	}
	
	protected void postConstructProcess(JeeslTranslationBean<L,D,LOC> bTranslation, JeeslFacesMessageBean bMessage,
										JeeslGraphicFacade<L,D,?,G,GT,?,?> fGraphic,
										JeeslWorkflowFacade<L,D,LOC,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,AC,WA,AB,AO,MT,MC,SR,RE,RA,WL,WF,WY,WD,FRC,USER> fApproval,
										JeeslIoRevisionFacade<L,D,?,?,?,?,?,RE,?,RA,?,?,?> fRevision)
	{
		super.initJeeslAdmin(bTranslation,bMessage);
		this.fGraphic=fGraphic;
		this.fWorkflow=fApproval;
		this.fRevision=fRevision;
		
		stageTypes.addAll(fApproval.allOrderedPositionVisible(fbWorkflow.getClassStageType()));
		transitionTypes.addAll(fApproval.allOrderedPositionVisible(fbWorkflow.getClassTransitionType()));
		permissionTypes.addAll(fApproval.allOrderedPositionVisible(fbWorkflow.getClassPermissionType()));
		modificationLevels.addAll(fApproval.allOrderedPositionVisible(fbWorkflow.getClassModificationLevel()));
		
		bots.addAll(fApproval.allOrderedPositionVisible(fbWorkflow.getClassBot()));
		try
		{
			initEntities();
			initMessageTemplates();
			initPageSettings();
		}
		catch (JeeslNotFoundException e) {e.printStackTrace();}
		
		Collections.sort(roles,cpRole);
		
		reloadProcesses();
		if(sbhProcess.isSelected())
		{
			process = fApproval.find(fbWorkflow.getClassProcess(),sbhProcess.getSelection());
			reloadProcess();
		}
	}
	
	protected void initMessageTemplates()
	{
		templates.addAll(fWorkflow.all(fbTemplate.getClassTemplate()));
	}
	protected abstract void initEntities() throws JeeslNotFoundException;
	
	protected void initPageSettings()
	{
		channels.addAll(fWorkflow.allOrderedPositionVisible(fbTemplate.getClassType()));
		
		roles.addAll(fWorkflow.allOrderedPositionVisible(fbSecurity.getClassRole()));
		
		sbhContext.setList(fWorkflow.allOrderedPositionVisible(fbWorkflow.getClassContext()));
		sbhContext.setDefault();
		
		if(debugOnInfo) {logger.info(AbstractLogMessage.reloaded(fbWorkflow.getClassContext(), sbhContext.getList()));}
	}
	
	public void cancelPermission() {reset(WorkflowProcesslResetHandler.build().none().permission(true));}
	public void cancelCommunication() {reset(WorkflowProcesslResetHandler.build().none().communication(true));}
	public void cancelAction() {reset(WorkflowProcesslResetHandler.build().none().action(true));}
	private void reset(WorkflowProcesslResetHandler reset)
	{
		if(reset.isDocuments()) {documents.clear();}
		if(reset.isDocument()) {document=null;}
		if(reset.isStages()) {stages.clear();}
		if(reset.isStage()) {stage=null;}
		if(reset.isPermissions()) {permissions.clear();;}
		if(reset.isPermission()) {permission=null;}
		if(reset.isTransistions()) {transitions.clear();}
		if(reset.isTransistion()) {transition=null;}
		if(reset.isCommunications()) {communications.clear();}
		if(reset.isCommunication()) {communication=null;}
		if(reset.isActions()) {actions.clear();}
		if(reset.isAction()) {action=null;}
	}
	
	@Override
	public void selectSbSingle(EjbWithId item) throws JeeslLockingException, JeeslConstraintViolationException
	{
		reset(WorkflowProcesslResetHandler.build().all());
		if(item instanceof JeeslWorkflowContext) 
		{
			reloadProcesses();
			sbhProcess.setSelection(null);
			sbhProcess.silentCallback();
		}
		else if(item instanceof JeeslWorkflowProcess)
		{
			process = fWorkflow.find(fbWorkflow.getClassProcess(),sbhProcess.getSelection());
			reloadDocuments();
			reloadStages();
		}
	}
	
	public void reloadProcesses()
	{
		sbhProcess.update(fWorkflow.allForContext(fbWorkflow.getClassProcess(),sbhContext.getSelection()));
		if(debugOnInfo){logger.info(AbstractLogMessage.reloaded(fbWorkflow.getClassProcess(), sbhProcess.getList(),sbhContext.getSelection()));}
	}
	
	public void reloadStages()
	{
		stages.clear();
		stages.addAll(fWorkflow.allForParent(fbWorkflow.getClassStage(),process));
	}
	
	private void reloadProcess()
	{
		reloadStages();
		reloadDocuments();
	}

	public void addProcess() throws JeeslNotFoundException
	{
		reset(WorkflowProcesslResetHandler.build().all());
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassProcess()));
		process = fbWorkflow.ejbProcess().build();
		process.setName(efLang.createEmpty(localeCodes));
		process.setDescription(efDescription.createEmpty(localeCodes));
		process.setContext(sbhContext.getSelection());
	}
	
	public void selectProcess() throws JeeslNotFoundException
	{
		reset(WorkflowProcesslResetHandler.build().all());
		logger.info(AbstractLogMessage.selectEntity(process));
		process = fWorkflow.find(fbWorkflow.getClassProcess(), process);
		process = efLang.persistMissingLangs(fWorkflow,localeCodes,process);
		process = efDescription.persistMissingLangs(fWorkflow,localeCodes,process);

	}
	
	public void saveProcess() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(process));
		process.setContext(fWorkflow.find(fbWorkflow.getClassContext(), process.getContext()));
		process = fWorkflow.save(process);
		reloadProcesses();
	}
	
	public void deleteProcess() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(process));
		fWorkflow.rm(process);
		reset(WorkflowProcesslResetHandler.build().all());
		reloadProcesses();
	}
	
	private void reloadDocuments()
	{
		documents.clear();
		documents.addAll(fWorkflow.allForParent(fbWorkflow.getClassDocument(),process));
		opDocument.setLazy(documents);
		logger.info(AbstractLogMessage.reloaded(fbWorkflow.getClassDocument(),opDocument.getOpList()));
	}
	
	public void addDocument()
	{
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassDocument()));
		document = fbWorkflow.ejbDocument().build(process,documents);
		document.setName(efLang.createEmpty(localeCodes));
		document.setDescription(efDescription.createEmpty(localeCodes));
	}
	
	public void selectDocument()
	{
		logger.info(AbstractLogMessage.selectEntity(document));
	}
	
	public void saveDocument() throws JeeslConstraintViolationException, JeeslLockingException
	{
		logger.info(AbstractLogMessage.saveEntity(document));
		document = fWorkflow.save(document);
		reloadDocuments();
	}
	
	public void addStage()
	{
		reset(WorkflowProcesslResetHandler.build().all().stages(false));
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassProcess()));
		stage = fbWorkflow.ejbStage().build(process,stages);
		stage.setName(efLang.createEmpty(localeCodes));
		stage.setDescription(efDescription.createEmpty(localeCodes));
		editStage = true;
	}
	
	public void saveStage() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(stage));
		reloadStageSelectOne();
		stage = fWorkflow.save(stage);
		reloadStages();
	}
	
	public void handleFileUpload(FileUploadEvent event) throws  JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		UploadedFile file = event.getFile();
		logger.info("Received file with a size of " +file.getSize());
		
		G graphic = null;
		try
		{
			graphic = fGraphic.fGraphic(fbWorkflow.getClassStage(),stage.getId());
		}
		catch (JeeslNotFoundException e)
		{
			GT type = fWorkflow.fByCode(fbSvg.getClassGraphicType(), JeeslGraphicType.Code.svg);
			graphic = fWorkflow.persist(fbSvg.efGraphic().build(type));
			stage.setGraphic(graphic);
			stage = fWorkflow.save(stage);
		}

		graphic.setData(file.getContents());
		graphic = fWorkflow.save(graphic);
	}
	
	private void reloadStageSelectOne()
	{
		stage.setProcess(fWorkflow.find(fbWorkflow.getClassProcess(), stage.getProcess()));
		if(stage.getType()!=null) {stage.setType(fWorkflow.find(fbWorkflow.getClassStageType(),stage.getType()));}
	}
	
	public void selectStage() throws JeeslNotFoundException
	{
		reset(WorkflowProcesslResetHandler.build().all().stages(false).stage(false));
		logger.info(AbstractLogMessage.selectEntity(stage));
		stage = fWorkflow.find(fbWorkflow.getClassStage(), stage);
		stage = efLang.persistMissingLangs(fWorkflow,localeCodes,stage);
		stage = efDescription.persistMissingLangs(fWorkflow,localeCodes,stage);
		editStage = false;
		reloadTransitions();
		reloadPermissions();
	}
	
	public void deleteStage() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(stage));
		fWorkflow.rm(stage);
		reset(WorkflowProcesslResetHandler.build().all());
		reloadStages();
	}
	
	private void reloadPermissions()
	{
		reset(WorkflowProcesslResetHandler.build().none().permissions(true));
		permissions.addAll(fWorkflow.allForParent(fbWorkflow.getClassPermission(),stage));
	}
	
	public void addPermission()
	{
		reset(WorkflowProcesslResetHandler.build().none().permission(true));
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassPermission()));
		permission = fbWorkflow.ejbPermission().build(stage,permissions);
	}
	
	public void savePermission() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(permission));
		permission.setRole(fWorkflow.find(fbSecurity.getClassRole(), permission.getRole()));
		permission.setType(fWorkflow.find(fbWorkflow.getClassPermissionType(), permission.getType()));
		permission.setModificationLevel(fWorkflow.find(fbWorkflow.getClassModificationLevel(), permission.getModificationLevel()));
		permission = fWorkflow.save(permission);
		reloadPermissions();
	}
	
	public void selectPermission() throws JeeslNotFoundException
	{
		reset(WorkflowProcesslResetHandler.build().none());
		logger.info(AbstractLogMessage.selectEntity(permission));
		permission = fWorkflow.find(fbWorkflow.getClassPermission(),permission);
	}
	
	public void deletePermission() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(permission));
		fWorkflow.rm(permission);
		reset(WorkflowProcesslResetHandler.build().none().permission(true));
		reloadPermissions();
	}
	
	private void reloadTransitions()
	{
		transitions.clear();
		transitions.addAll(fWorkflow.allForParent(fbWorkflow.getClassTransition(), stage));
	}
	
	public void addTransition()
	{
		reset(WorkflowProcesslResetHandler.build().none().transistion(true));
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassTransition()));
		transition = efTransition.build(stage,transitions);
		transition.setName(efLang.createEmpty(localeCodes));
		transition.setDescription(efDescription.createEmpty(localeCodes));
		transition.setConfirmation(efDescription.createEmpty(localeCodes));
		efTransition.ejb2nnb(transition,nnb);
		editTransition = true;
	}
	
	public void saveTransition() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(transition));
		transition.setDestination(fWorkflow.find(fbWorkflow.getClassStage(), transition.getDestination()));
		transition.setType((fWorkflow.find(fbWorkflow.getClassTransitionType(), transition.getType())));
		if(transition.getRole()!=null) {transition.setRole(fWorkflow.find(fbSecurity.getClassRole(),transition.getRole()));}
		efTransition.nnb2ejb(transition,nnb);
		transition = fWorkflow.save(transition);
		reloadTransitions();
		reloadActions();
		reloadCommunications();
		reloadRequired();
		bMessage.growlSuccessSaved();
	}
	
	public void selectTransition() throws JeeslNotFoundException, JeeslConstraintViolationException, JeeslLockingException
	{
		reset(WorkflowProcesslResetHandler.build().none().action(true).actions(true).communication(true).communications(true));
		logger.info(AbstractLogMessage.selectEntity(transition));
		transition = fWorkflow.find(fbWorkflow.getClassTransition(),transition);
		transition = efLang.persistMissingLangs(fWorkflow,localeCodes,transition);
		transition = efDescription.persistMissingLangs(fWorkflow,localeCodes,transition);
		transition.setConfirmation(efDescription.persistMissingLangs(fWorkflow,localeCodes,transition.getConfirmation())); transition = fWorkflow.save(transition);
		efTransition.ejb2nnb(transition,nnb);
		editTransition = false;
		reloadActions();
		reloadCommunications();
		reloadRequired();
	}
	
	public void deleteTransition() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(transition));
		fWorkflow.rm(transition);
		reset(WorkflowProcesslResetHandler.build().none().transistion(true));
		reloadTransitions();
	}
	
	private void reloadCommunications()
	{
		reset(WorkflowProcesslResetHandler.build().none().communications(true));
		communications.addAll(fWorkflow.allForParent(fbWorkflow.getClassCommunication(),transition));
	}
	
	public void addCommunication()
	{
		reset(WorkflowProcesslResetHandler.build().none().communication(true));
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassTransition()));
		communication = fbWorkflow.ejbCommunication().build(transition,communications);
	}
	
	public void saveCommunication() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(transition));
		communication.setTemplate(fWorkflow.find(fbTemplate.getClassTemplate(), communication.getTemplate()));
		communication.setRole(fWorkflow.find(fbSecurity.getClassRole(), communication.getRole()));
		communication.setScope(fWorkflow.find(fbRevision.getClassEntity(), communication.getScope()));
		communication.setChannel(fWorkflow.find(fbTemplate.getClassType(), communication.getChannel()));
		communication = fWorkflow.save(communication);
		reloadCommunications();
	}
	
	public void selectCommunication() throws JeeslNotFoundException
	{
		reset(WorkflowProcesslResetHandler.build().none());
		logger.info(AbstractLogMessage.selectEntity(communication));
		communication = fWorkflow.find(fbWorkflow.getClassCommunication(),communication);
	}
	
	public void deleteCommunication() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(communication));
		fWorkflow.rm(communication);
		reset(WorkflowProcesslResetHandler.build().none().communication(true));
		reloadCommunications();
	}

	private void reloadActions()
	{
		reset(WorkflowProcesslResetHandler.build().none().actions(true));
		actions.addAll(fWorkflow.allForParent(fbWorkflow.getClassAction(),transition));
	}
	
	public void addAction()
	{
		reset(WorkflowProcesslResetHandler.build().none().action(true));
		logger.info(AbstractLogMessage.addEntity(fbWorkflow.getClassAction()));
		action = fbWorkflow.ejbAction().build(transition,actions);
		if(!bots.isEmpty()) {action.setBot(bots.get(0));}
	}
	
	@SuppressWarnings("unchecked")
	public void saveAction() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.saveEntity(action)+" cmd:"+action.getCallbackCommand());
		action.setBot(fWorkflow.find(fbWorkflow.getClassBot(), action.getBot()));
		
		if(action.getEntity()!=null) {action.setEntity(fWorkflow.find(fbRevision.getClassEntity(),action.getEntity()));}
		if(action.getAttribute()!=null)
		{
			action.setAttribute(fWorkflow.find(fbRevision.getClassAttribute(),action.getAttribute()));
			
			if(debugOnInfo) {logger.info("cOption!=null?"+(cOption!=null)+" option!=null?"+(option!=null));}
			
			logger.info("Saving option:"+option+" for "+cOption.getName());
			AO id = (AO)fWorkflow.find(cOption,option);
			action.setOption(id);
		}	
		
		action = fWorkflow.save(action);
		reloadActions();
	}
	
	public void selectAction() throws JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.selectEntity(action));
		action = fWorkflow.find(fbWorkflow.getClassAction(),action);
		changeEntity();
	}
	
	public void deleteAction() throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException
	{
		logger.info(AbstractLogMessage.rmEntity(action));
		fWorkflow.rm(action);
		reset(WorkflowProcesslResetHandler.build().none().action(true));
		reloadActions();
	}
	
	public void changeBot()
	{
		logger.info(AbstractLogMessage.selectOneMenuChange(action.getBot()));
		action.setBot(fWorkflow.find(fbWorkflow.getClassBot(), action.getBot()));
	}
	
	public void changeEntity()
	{
		logger.info(AbstractLogMessage.selectOneMenuChange(action.getEntity()));
		attributes.clear();
		if(action.getEntity()==null) {action.setAttribute(null);}
		else
		{
			action.setEntity(fWorkflow.find(fbRevision.getClassEntity(),action.getEntity()));
			action.setEntity(fRevision.load(fbRevision.getClassEntity(),action.getEntity()));
			attributes.addAll(action.getEntity().getAttributes());
			
			if(attributes.isEmpty()) {action.setAttribute(null);}
			else
			{
				if(action.getAttribute()==null) {action.setAttribute(attributes.get(0));}
				else
				{
					if(!attributes.contains(action.getAttribute())){action.setAttribute(attributes.get(0));}
				}
			}
		}
		changeAttribute();
	}
	
	@SuppressWarnings("unchecked")
	public void changeAttribute()
	{
		logger.info(AbstractLogMessage.selectOneMenuChange(action.getAttribute()));
		options.clear();
		
		if(action.getAttribute()!=null)
		{
			logger.info("The following attribute is selected: "+action.getAttribute().toString());
			logger.info("Now checking for a entity, is there something? "+(action.getAttribute().getEntity()!=null));
			action.setAttribute(fWorkflow.find(fbRevision.getClassAttribute(),action.getAttribute()));
			logger.info("After find, is there something? "+(action.getAttribute().getEntity()!=null));
		}
		
		if(action.getAttribute()!=null && action.getAttribute().getEntity()!=null)
		{
			logger.info("Evaluating "+action.getAttribute().getEntity().getCode());
			try
			{
				cOption = (Class<EjbWithPosition>)Class.forName(action.getAttribute().getEntity().getCode()).asSubclass(EjbWithPosition.class);
				options.addAll(fWorkflow.allOrderedPosition(cOption));
			}
			catch (ClassNotFoundException e) {e.printStackTrace();}
			logger.info("Options: "+options.size());
			if(action.getOption()!=null) {option = action.getOption().getId();}
			else {option=null;}
		}
	}
	
	private void reloadRequired()
	{
		transition = fWorkflow.loadTransition(transition);
		opDocument.setTbList(transition.getDocuments());
	}
	
	public void prepareAddRequired()
	{
		logger.info("required:"+opDocument.getTbList().size()+" availble:"+opDocument.getOpList().size());
	}
	public void selectRequired()
	{
		logger.info(AbstractLogMessage.selectEntity(opDocument.getTb()));
	}
	
    @SuppressWarnings("unchecked")
	@Override public void addOpEntity(EjbWithId ejb) throws JeeslLockingException, JeeslConstraintViolationException
	{
    	WPD d = (WPD)ejb;
    	if(!transition.getDocuments().contains(d))
    	{
    		transition.getDocuments().add(d);
    		transition = fWorkflow.saveTransaction(transition);
    		reloadDocuments();
    	}
	}
	
    @SuppressWarnings("unchecked")
	@Override public void rmOpEntity(EjbWithId ejb) throws JeeslLockingException, JeeslConstraintViolationException
	{
		WPD d = (WPD)ejb;
    	if(transition.getDocuments().contains(d))
    	{
    		transition.getDocuments().remove(d);
    		transition = fWorkflow.saveTransaction(transition);
    		reloadDocuments();
    	}
	}
	
	public void reorderProcesses() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fWorkflow,sbhProcess.getList());}
	public void reorderTransitions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fWorkflow,transitions);}
	public void reorderStages() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fWorkflow,stages);}
	public void reorderPermissions() throws JeeslConstraintViolationException, JeeslLockingException {PositionListReorderer.reorder(fWorkflow,permissions);}
}