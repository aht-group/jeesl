package org.jeesl.factory.xml.module.workflow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jeesl.api.facade.module.JeeslWorkflowFacade;
import org.jeesl.factory.builder.module.WorkflowFactoryBuilder;
import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
import org.jeesl.factory.xml.system.status.XmlTypeFactory;
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
import org.jeesl.model.xml.jeesl.QueryWf;
import org.jeesl.model.xml.module.workflow.Permissions;
import org.jeesl.model.xml.module.workflow.Stage;
import org.jeesl.util.comparator.ejb.PositionComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStageFactory<L extends JeeslLang, D extends JeeslDescription,
								WS extends JeeslWorkflowStage<L,D,?,WST,WSP,WT,?>,
								WST extends JeeslWorkflowStageType<L,D,WST,?>,
								WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
								WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
								WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
								WT extends JeeslWorkflowTransition<L,D,?,WS,WTT,?,?>,
								WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
								SR extends JeeslSecurityRole<L,D,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlStageFactory.class);
	
	private final String localeCode;
	private final Stage q;
	
	private XmlTypeFactory<L,D,WST> xfType;
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlTransitionFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xfTransition;
	private XmlPermissionFactory<L,D,WS,WSP,WPT,WML,SR> xfPermission;
	
	private WorkflowFactoryBuilder<L,D,?,?,?,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow;
	private JeeslWorkflowFacade   <L,D,?,?,?,?,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fWorkflow;
	
	public XmlStageFactory(QueryWf query) {this(query.getLocaleCode(),query.getStage());}
	public XmlStageFactory(String localeCode, Stage q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetType()) {xfType = new XmlTypeFactory<>(localeCode,q.getType());}
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(q.isSetTransition()) {xfTransition = new XmlTransitionFactory<>(localeCode,q.getTransition().get(0));}
		if(q.isSetPermissions() && q.getPermissions().isSetPermission()) {xfPermission = new XmlPermissionFactory<>(localeCode,q.getPermissions().getPermission().get(0));}
	}
	
	public void lazy(WorkflowFactoryBuilder<L,D,  ?,?,?,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fbWorkflow,
						JeeslWorkflowFacade<L,D,?,?,?,?,WS,WST,WSP,?,?,?,WT,WTT,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?> fWorkflow)
	{
		this.fbWorkflow=fbWorkflow;
		this.fWorkflow=fWorkflow;
	}
	
	public static Stage build(){return new Stage();}
	public static Stage build(String label, Double progress)
	{
		Stage xml = build();
		xml.setLabel(label);
		if(progress!=null) {xml.setProgress(progress);}
		return xml;
	}
	
	public Stage build(WS stage)
	{
		Stage xml = build();
		if(q.isSetId()) {xml.setId(stage.getId());}
		if(q.isSetPosition()) {xml.setPosition(stage.getPosition());}
		if(q.isSetType()) {xml.setType(xfType.build(stage.getType()));}
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(stage.getName()));}
		if(q.isSetDescriptions()) {xml.setDescriptions(xfDescription.create(stage.getDescription()));}
		
		if(q.isSetTransition())
		{
			List<WT> transitions = new ArrayList<WT>();
			if(fbWorkflow!=null && fWorkflow!=null) {transitions.addAll(fWorkflow.allForParent(fbWorkflow.getClassTransition(), stage));}
			else {transitions.addAll(stage.getTransitions());}
			Collections.sort(transitions, new PositionComparator<WT>());
			for(WT transition : transitions)
			{
				xml.getTransition().add(xfTransition.build(transition));
			}
		}
		
		if(q.isSetPermissions())
		{
			Permissions xPermissions = XmlPermissionsFactory.build();
			if(q.getPermissions().isSetPermission())
			{
				
				List<WSP> ePermissions = new ArrayList<WSP>();
				if(fbWorkflow!=null && fWorkflow!=null) {ePermissions.addAll(fWorkflow.allForParent(fbWorkflow.getClassPermission(), stage));}
				else {ePermissions.addAll(stage.getPermissions());}
				for(WSP permission : ePermissions)
				{
					xPermissions.getPermission().add(xfPermission.build(permission));
				}
			}
			xml.setPermissions(xPermissions);
		}
		
		if(q.isSetLabel() && localeCode!=null && stage.getName()!=null && stage.getName().containsKey(localeCode)) {xml.setLabel(stage.getName().get(localeCode).getLang());}
		
		return xml;
	}
}