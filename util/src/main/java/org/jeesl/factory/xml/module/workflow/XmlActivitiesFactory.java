package org.jeesl.factory.xml.module.workflow;

import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowContext;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStageType;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.model.xml.module.workflow.Activities;
import org.jeesl.model.xml.module.workflow.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlActivitiesFactory<L extends JeeslLang, D extends JeeslDescription,
								WX extends JeeslWorkflowContext<L,D,WX,?>,
								WP extends JeeslWorkflowProcess<L,D,WX,WS>,
								WS extends JeeslWorkflowStage<L,D,WP,WST,?,WT,?>,
								WST extends JeeslWorkflowStageType<L,D,WST,?>,
								WT extends JeeslWorkflowTransition<L,D,?,WS,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(XmlActivitiesFactory.class);
	
	
	@SuppressWarnings("unused") private final String localeCode;
	@SuppressWarnings("unused") private final Activities q;
	
	public XmlActivitiesFactory(String localeCode, Activities q)
	{
		this.localeCode=localeCode;
		this.q=q;
	}
	
	public static Activities build() {return new Activities();}
	public static Activities build(Activity activity) {Activities xml = build(); xml.getActivity().add(activity); return xml;}
}