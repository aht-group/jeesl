package org.jeesl.factory.xml.module.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.xml.io.locale.status.XmlContextFactory;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
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
import org.jeesl.model.xml.io.db.query.QueryWf;
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
								SR extends JeeslSecurityRole<L,D,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlProcessFactory.class);
	
	private final String localeCode;
	private final org.jeesl.model.xml.module.workflow.Process q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlContextFactory<L,D,WX> xfContext;
	private XmlStageFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xfStage;
	
	private WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,?,?,?,?,WT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow;
	private JeeslWorkflowFacade<WP,WS,?,?,WT,?,?,?,?,?,?,?> fWorkflow;
	
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
					SR extends JeeslSecurityRole<L,D,?,?,?,?>>
			XmlProcessFactory<L,D,WX,WP,WPD,WS,WST,WSP,WPT,WML,WT,WTT,SR> instance(String localeCode, XmlWorkflowQuery.Key key)
	{
		return new XmlProcessFactory<>(XmlWorkflowQuery.get(key, localeCode));
	}
	
	public XmlProcessFactory(QueryWf query) {this(query.getLocaleCode(),query.getProcess());}
	public XmlProcessFactory(String localeCode, org.jeesl.model.xml.module.workflow.Process q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(Objects.nonNull(q.getContext())) {xfContext = new XmlContextFactory<>(localeCode,q.getContext());}
		if(ObjectUtils.isNotEmpty(q.getStage())) {xfStage = new XmlStageFactory<>(localeCode,q.getStage().get(0));}
	}
	
	public void lazy(WorkflowFactoryBuilder<L,D,WX,WP,WPD,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow,
					JeeslWorkflowFacade<WP,WS,?,?,WT,WTT,?,?,?,?,?,?> fWorkflow)
	{
		this.fbWorkflow=fbWorkflow;
		this.fWorkflow=fWorkflow;
		if(xfStage!=null) {xfStage.lazy(fbWorkflow,fWorkflow);}
	}
	
	public static org.jeesl.model.xml.module.workflow.Process build(){return new org.jeesl.model.xml.module.workflow.Process();}
	
	public org.jeesl.model.xml.module.workflow.Process build(WP process)
	{
		org.jeesl.model.xml.module.workflow.Process xml = build();
		if(Objects.nonNull(q.getId())) {xml.setId(process.getId());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(process.getPosition());}
		if(Objects.nonNull(q.getCode())) {xml.setCode(process.getCode());}
		
		if(ObjectUtils.allNotNull(localeCode,q.getLabel()) && process.getName().containsKey(localeCode)) {xml.setLabel(process.getName().get(localeCode).getLang());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(process.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(process.getDescription()));}
		if(Objects.nonNull(q.getContext())) {xml.setContext(xfContext.build(process.getContext()));}
		
		if(ObjectUtils.isNotEmpty(q.getStage()))
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