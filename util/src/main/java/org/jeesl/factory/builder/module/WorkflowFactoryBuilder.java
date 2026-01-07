package org.jeesl.factory.builder.module;

import java.util.Comparator;

import org.jeesl.controller.util.comparator.ejb.module.workflow.EjbWorkflowProcessComparator;
import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowActionFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowActivityFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowCommunicationFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowDelegateFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowDocumentFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowLinkFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowNotificationFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowPermissionFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowProcessFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowStageFactory;
import org.jeesl.factory.ejb.module.workflow.EjbWorkflowTransitionFactory;
import org.jeesl.factory.xml.module.workflow.XmlProcessFactory;
import org.jeesl.factory.xml.module.workflow.XmlStageFactory;
import org.jeesl.factory.xml.module.workflow.XmlTransitionFactory;
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
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.model.xml.io.db.query.QueryWf;
import org.jeesl.util.query.xml.module.XmlWorkflowQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowFactoryBuilder<L extends JeeslLang, D extends JeeslDescription,
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
									AA extends JeeslWorkflowAction<WT,AB,AO,RE,RA>,
									AB extends JeeslWorkflowBot<AB,L,D,?>,
									AO extends EjbWithId,
									MT extends JeeslIoTemplate<L,D,?,?,?,?>,
									MC extends JeeslTemplateChannel<L,D,MC,?>,
									SR extends JeeslSecurityRole<L,D,?,?,?,?>,
									RE extends JeeslRevisionEntity<L,D,?,?,RA,?>,
									RA extends JeeslRevisionAttribute<L,D,RE,?,?>,
									WL extends JeeslWorkflowLink<WF,RE>,
									WF extends JeeslWorkflow<WP,WS,WY,USER>,
									WY extends JeeslWorkflowActivity<WT,WF,WD,FRC,USER>,
									WD extends JeeslWorkflowDelegate<WY,USER>,
									FRC extends JeeslFileContainer<?,?>,
									USER extends JeeslUser<SR>>
				extends AbstractFactoryBuilder<L,D>
{
	private static final long serialVersionUID = 1L;

	final static Logger logger = LoggerFactory.getLogger(WorkflowFactoryBuilder.class);
	
	private final Class<WX> cContext; public Class<WX> getClassContext() {return cContext;}
	private final Class<WP> cProcess; public Class<WP> getClassProcess() {return cProcess;}
	private final Class<WPD> cDocument; public Class<WPD> getClassDocument() {return cDocument;}
	private final Class<WS> cStage; public Class<WS> getClassStage() {return cStage;}
	private final Class<WST> cStageType; public Class<WST> getClassStageType() {return cStageType;}
	private final Class<WSP> cPermission; public Class<WSP> getClassPermission() {return cPermission;}
	private final Class<WPT> cPermissionType; public Class<WPT> getClassPermissionType() {return cPermissionType;}
	private final Class<WML> cModificationLevel; public Class<WML> getClassModificationLevel() {return cModificationLevel;}
	private final Class<WSN> cStageNotification; public Class<WSN> getClassStageNotification() {return cStageNotification;}
	private final Class<WT> cTransition; public Class<WT> getClassTransition() {return cTransition;}
	private final Class<WTT> cTransitionType; public Class<WTT> getClassTransitionType() {return cTransitionType;}
	private final Class<WC> cCommunication; public Class<WC> getClassCommunication() {return cCommunication;}
	private final Class<AA> cAction; public Class<AA> getClassAction() {return cAction;}
	private final Class<AB> cBot; public Class<AB> getClassBot() {return cBot;}
	private final Class<WL> cLink; public Class<WL> getClassLink() {return cLink;}
	private final Class<WF> cWorkflow; public Class<WF> getClassWorkflow() {return cWorkflow;}
	private final Class<WY> cActivity; public Class<WY> getClassActivity() {return cActivity;}
	private final Class<WD> cDelegate; public Class<WD> getClassDelegate() {return cDelegate;}
	
	public WorkflowFactoryBuilder(final Class<L> cL, final Class<D> cD,
									final Class<WX> cContext,
									final Class<WP> cProcess,
									final Class<WPD> cDocument,
									final Class<WS> cStage,
									final Class<WST> cStageType,
									final Class<WSP> cPermission,
									final Class<WPT> cPermissionType,
									final Class<WML> cModificationLevel,
									final Class<WSN> cStageNotification,
									final Class<WT> cTransition,
									final Class<WTT> cTransitionType,
									final Class<WC> cCommunication,
									final Class<AA> cAction,
									final Class<AB> cBot,
									final Class<WL> cLink,
									final Class<WF> cWorkflow,
									final Class<WY> cActivity,
									final Class<WD> cDelegate)
	{
		super(cL,cD);
		this.cContext=cContext;
		this.cProcess=cProcess;
		this.cDocument=cDocument;
		this.cStage=cStage;
		this.cStageType=cStageType;
		this.cPermission=cPermission;
		this.cPermissionType=cPermissionType;
		this.cModificationLevel=cModificationLevel;
		this.cStageNotification=cStageNotification;
		this.cTransition=cTransition;
		this.cTransitionType=cTransitionType;
		this.cCommunication=cCommunication;
		this.cAction=cAction;
		this.cBot=cBot;
		this.cLink=cLink;
		this.cWorkflow=cWorkflow;
		this.cActivity=cActivity;
		this.cDelegate=cDelegate;
	}
	
	public EjbWorkflowProcessFactory<WP> ejbProcess() {return new EjbWorkflowProcessFactory<>(cProcess);}
	public EjbWorkflowDocumentFactory<WP,WPD> ejbDocument() {return new EjbWorkflowDocumentFactory<>(cDocument);}
	public EjbWorkflowStageFactory<WP,WS> ejbStage() {return new EjbWorkflowStageFactory<>(cStage);}
	public EjbWorkflowPermissionFactory<WS,WSP,WML,SR> ejbPermission() {return new EjbWorkflowPermissionFactory<>(cPermission);}
	public EjbWorkflowNotificationFactory<WS,WSN,MT,MC,SR,RE> ejbNotification() {return new EjbWorkflowNotificationFactory<>(cStageNotification);}
	public EjbWorkflowTransitionFactory<WS,WT> ejbTransition() {return new EjbWorkflowTransitionFactory<>(cTransition);}
	public EjbWorkflowCommunicationFactory<WT,WC,MT,MC,SR,RE> ejbCommunication() {return new EjbWorkflowCommunicationFactory<>(cCommunication);}
	public EjbWorkflowActionFactory<WT,AA,AB,AO,RE,RA> ejbAction() {return new EjbWorkflowActionFactory<>(cAction);}
	public EjbWorkflowLinkFactory<RE,WL,WF> ejbLink() {return new EjbWorkflowLinkFactory<>(cLink);}
	public EjbWorkflowFactory<WP,WS,WL,WF> ejbWorkflow() {return new EjbWorkflowFactory<>(cWorkflow);}
	public EjbWorkflowActivityFactory<WT,WF,WY,USER> ejbActivity() {return new EjbWorkflowActivityFactory<>(cActivity);}
	public EjbWorkflowDelegateFactory<WY,WD,USER> ejbDelegate() {return new EjbWorkflowDelegateFactory<>(cDelegate);}
	
	public Comparator<WP> cpProcess(EjbWorkflowProcessComparator.Type type) {return new EjbWorkflowProcessComparator<WX,WP>().factory(type);}
	
	public XmlProcessFactory<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,SR> xmlProcess(String localeCode, XmlWorkflowQuery.Key key) {return new XmlProcessFactory<>(XmlWorkflowQuery.get(key, localeCode));}
	public XmlStageFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xmlStage(QueryWf q) {return new XmlStageFactory<>(q);}
	public XmlTransitionFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xmlTransition(String localeCode, XmlWorkflowQuery.Key key) {return new XmlTransitionFactory<>(XmlWorkflowQuery.get(key, localeCode));}
}