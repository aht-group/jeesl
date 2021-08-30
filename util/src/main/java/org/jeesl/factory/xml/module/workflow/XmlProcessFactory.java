package org.jeesl.factory.xml.module.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlContextFactory;
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
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.model.xml.jeesl.QueryWf;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.jeesl.util.query.xml.module.XmlWorkflowQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlProcessFactory<L extends JeeslLang, D extends JeeslDescription,
								WX extends JeeslWorkflowContext<L,D,WX,?>,
								WP extends JeeslWorkflowProcess<L,D,WX,WS>,
								WPD extends JeeslWorkflowDocument<L,D,WP>,
								WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
								WST extends JeeslWorkflowStageType<L,D,WST,?>,
								WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
								WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
								WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
								WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,?,?>,
								WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
								SR extends JeeslSecurityRole<L,D,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlProcessFactory.class);
	
	private final String localeCode;
	private final org.jeesl.model.xml.module.workflow.Process q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlContextFactory<L,D,WX> xfContext;
	private XmlStageFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xfStage;
	
	private WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,?,?,?,?,WT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow;
	private JeeslWorkflowFacade<L,D,?,WX,WP,WPD,WS,WST,?,?,?,?,WT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fWorkflow;
	
	private static <L extends JeeslLang, D extends JeeslDescription,
					WX extends JeeslWorkflowContext<L,D,WX,?>,
					WP extends JeeslWorkflowProcess<L,D,WX,WS>,
					WPD extends JeeslWorkflowDocument<L,D,WP>,
					WS extends JeeslWorkflowStage<L,D,WP,WST,WSP,WT,?>,
					WST extends JeeslWorkflowStageType<L,D,WST,?>,
					WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
					WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
					WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
					WT extends JeeslWorkflowTransition<L,D,WPD,WS,WTT,?,?>,
					WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
					SR extends JeeslSecurityRole<L,D,?,?,?,?,?>>
			XmlProcessFactory<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,SR> instance(String localeCode, XmlWorkflowQuery.Key key)
	{
		return new XmlProcessFactory<>(XmlWorkflowQuery.get(key, localeCode));
	}
	
	public XmlProcessFactory(QueryWf query) {this(query.getLocaleCode(),query.getProcess());}
	public XmlProcessFactory(String localeCode, org.jeesl.model.xml.module.workflow.Process q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(q.isSetContext()) {xfContext = new XmlContextFactory<>(localeCode,q.getContext());}
		if(q.isSetStage()) {xfStage = new XmlStageFactory<>(localeCode,q.getStage().get(0));}
	}
	
	public void lazy(WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow,
					JeeslWorkflowFacade<L,D,?,WX,WP,WPD,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fWorkflow)
	{
		this.fbWorkflow=fbWorkflow;
		this.fWorkflow=fWorkflow;
		if(xfStage!=null) {xfStage.lazy(fbWorkflow,fWorkflow);}
	}
	
	public static org.jeesl.model.xml.module.workflow.Process build(){return new org.jeesl.model.xml.module.workflow.Process();}
	
	public org.jeesl.model.xml.module.workflow.Process build(WP process)
	{
		org.jeesl.model.xml.module.workflow.Process xml = build();
		if(q.isSetId()) {xml.setId(process.getId());}
		if(q.isSetPosition()) {xml.setPosition(process.getPosition());}
		if(q.isSetCode()) {xml.setCode(process.getCode());}
		
		if(localeCode!=null && q.isSetLabel() && process.getName().containsKey(localeCode)) {xml.setLabel(process.getName().get(localeCode).getLang());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(process.getName()));}
		if(q.isSetDescriptions()) {xml.setDescriptions(xfDescription.create(process.getDescription()));}
		if(q.isSetContext()) {xml.setContext(xfContext.build(process.getContext()));}
		
		if(q.isSetStage())
		{
			List<WS> stages = new ArrayList<WS>();
			if(fbWorkflow!=null && fWorkflow!=null) {stages.addAll(fWorkflow.allForParent(fbWorkflow.getClassStage(), process));}
			else {stages.addAll(process.getStages());}
			Collections.sort(stages, new PositionComparator<WS>());
			for(WS stage : stages)
			{
				xml.getStage().add(xfStage.build(stage));
			}
		}
		
		return xml;
	}
}