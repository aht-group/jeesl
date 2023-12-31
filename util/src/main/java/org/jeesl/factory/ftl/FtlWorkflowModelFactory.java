package org.jeesl.factory.ftl;

import java.util.HashMap;
import java.util.Map;

import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflowActivity;
import org.jeesl.interfaces.model.module.workflow.msg.JeeslWorkflowStageNotification;
import org.exlp.util.io.StringUtil;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowDocument;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtlWorkflowModelFactory <L extends JeeslLang, D extends JeeslDescription,
										WP extends JeeslWorkflowProcess<L,D,?,WS>,
										WPD extends JeeslWorkflowDocument<L,D,WP>,
										WS extends JeeslWorkflowStage<L,D,WP,?,?,WT,?>,
										WSN extends JeeslWorkflowStageNotification<WS,?,?,?,?>,
										WT extends JeeslWorkflowTransition<L,D,WPD,WS,?,?,?>,
										WF extends JeeslWorkflow<WP,WS,WY,USER>,
										WY extends JeeslWorkflowActivity<WT,WF,?,?,USER>,
										USER extends JeeslUser<?>
>
{
	final static Logger logger = LoggerFactory.getLogger(FtlWorkflowModelFactory.class);
		
	public Map<String,Object> build(String localeCode, WY activity, USER user)
	{		
		Map<String,Object> model = new HashMap<String,Object>();
		
		activity(localeCode,model,activity);
		recipient(localeCode,model,user);
		process(localeCode,model,activity.getWorkflow().getProcess());
		
		model.put("wfRecipientStage", activity.getTransition().getDestination().getName().get(localeCode).getLang());
		
		return model;
	}
	
	public Map<String,Object> build(String localeCode, WSN notification, USER user)
	{		
		Map<String,Object> model = new HashMap<String,Object>();
		
//		activity(localeCode,model,activity);
		recipient(localeCode,model,user);
		process(localeCode,model,notification.getStage().getProcess());
		
		model.put("wfStageOverdueHours", notification.getOverdueHours());
		
		return model;
	}
	
	private void recipient(String localeCode, Map<String,Object> model, USER user)
	{
		model.put("emailRecipientName", user.getFirstName()+" "+user.getLastName());	
	}
	
	private void activity(String localeCode, Map<String,Object> model, WY activity)
	{
		model.put("wfInitiatorName", activity.getUser().getFirstName()+" "+activity.getUser().getLastName());
		model.put("wfInitiatorStage", activity.getTransition().getSource().getName().get(localeCode).getLang());
		model.put("wfActivityRemark", activity.getRemark());
		model.put("wfActivityDate", activity.getRecord().toString());
		model.put("wfActivityTransition", activity.getTransition().getName().get(localeCode).getLang());
	}
	
	private void process(String localeCode, Map<String,Object> model, WP process)
	{
		model.put("wfContext", process.getContext().getName().get(localeCode).getLang());
		model.put("wfProcess", process.getName().get(localeCode).getLang());
	}
	
	public void debug(Map<String,Object> model)
	{
		logger.info(StringUtil.stars());
		for(String key : model.keySet())
		{
			logger.info(key+": "+model.get(key).toString());
		}
	}
}