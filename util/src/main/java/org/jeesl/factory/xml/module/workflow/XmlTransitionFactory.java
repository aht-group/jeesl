package org.jeesl.factory.xml.module.workflow;

import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
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
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.model.xml.io.db.query.QueryWf;
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
									SR extends JeeslSecurityRole<L,D,?,?,?,?>>
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
		if(Objects.nonNull(q.getLangs())) {xfLangs = new XmlLangsFactory<>(q.getLangs());}
		if(Objects.nonNull(q.getDescriptions())) {xfDescription = new XmlDescriptionsFactory<>(q.getDescriptions());}
		if(Objects.nonNull(q.getStage())) {xfStage = new XmlStageFactory<>(localeCode,q.getStage());}
	}
	
	public static Transition build(){return new Transition();}
	
	public Transition build(WT transition)
	{
		Transition xml = build();
		if(Objects.nonNull(q.getId())) {xml.setId(transition.getId());}
		if(Objects.nonNull(q.getPosition())) {xml.setPosition(transition.getPosition());}
		
		if(ObjectUtils.allNotNull(localeCode,q.getLabel()) && transition.getName().containsKey(localeCode)) {xml.setLabel(transition.getName().get(localeCode).getLang());}
		
		if(Objects.nonNull(q.getLangs())) {xml.setLangs(xfLangs.getUtilsLangs(transition.getName()));}
		if(Objects.nonNull(q.getDescriptions())) {xml.setDescriptions(xfDescription.create(transition.getDescription()));}
		if(Objects.nonNull(q.getStage())) {xml.setStage(xfStage.build(transition.getDestination()));}
		return xml;
	}
}