package org.jeesl.factory.xml.module.workflow;

import org.jeesl.factory.xml.system.lang.XmlDescriptionsFactory;
import org.jeesl.factory.xml.system.lang.XmlLangsFactory;
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
import org.jeesl.model.xml.module.workflow.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTransitionFactory<L extends JeeslLang, D extends JeeslDescription,
									WS extends JeeslWorkflowStage<L,D,?,WST,WSP,WT,?>,
									WST extends JeeslWorkflowStageType<L,D,WST,?>,
									WSP extends JeeslWorkflowStagePermission<WS,WPT,WML,SR>,
									WPT extends JeeslWorkflowPermissionType<L,D,WPT,?>,
									WML extends JeeslWorkflowModificationLevel<L,D,WML,?>,
									WT extends JeeslWorkflowTransition<L,D,?,WS,WTT,?,?>,
									WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
									SR extends JeeslSecurityRole<L,D,?,?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlTransitionFactory.class);
	

	private final String localeCode;
	private final Transition q;
	
	private XmlLangsFactory<L> xfLangs;
	private XmlDescriptionsFactory<D> xfDescription;
	private XmlStageFactory<L,D,WS,WST,WSP,WPT,WML,WT,WTT,SR> xfStage;
	
	public XmlTransitionFactory(QueryWf query) {this(query.getLocaleCode(),query.getTransition());}
	public XmlTransitionFactory(String localeCode, Transition q)
	{
		this.localeCode=localeCode;
		this.q=q;
		if(q.isSetLangs()) {xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(q.isSetDescriptions()) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(q.isSetStage()) {xfStage = new XmlStageFactory<>(localeCode,q.getStage());}
	}
	
	public static Transition build(){return new Transition();}
	
	public Transition build(WT transition)
	{
		Transition xml = build();
		if(q.isSetId()) {xml.setId(transition.getId());}
		if(q.isSetPosition()) {xml.setPosition(transition.getPosition());}
		
		if(localeCode!=null && q.isSetLabel() && transition.getName().containsKey(localeCode)) {xml.setLabel(transition.getName().get(localeCode).getLang());}
		
		if(q.isSetLangs()) {xml.setLangs(xfLangs.getUtilsLangs(transition.getName()));}
		if(q.isSetDescriptions()) {xml.setDescriptions(xfDescription.create(transition.getDescription()));}
		if(q.isSetStage()) {xml.setStage(xfStage.build(transition.getDestination()));}
		return xml;
	}
}