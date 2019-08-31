package org.jeesl.factory.sql.module.workflow;

import org.jeesl.interfaces.model.module.workflow.instance.JeeslApprovalWorkflow;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.exception.ejb.UtilsNotFoundException;

public class SqlWorkflowFactory
{
	final static Logger logger = LoggerFactory.getLogger(SqlWorkflowFactory.class);
	
	public static <WS extends JeeslWorkflowStage<?,?,?,?,?,?,?>,
					WF extends JeeslApprovalWorkflow<?,WS,?>>
						String updateCurrentStage(Class<WF> c, WF workflow, WS stage)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		try{sb.append(ReflectionUtil.toTable(c));} catch (UtilsNotFoundException e){e.printStackTrace();}
		sb.append(" SET ").append(JeeslApprovalWorkflow.Attributes.currentStage).append("_id=").append(stage.getId());
		sb.append(" WHERE id=").append(workflow.getId());
		sb.append(";");
		return sb.toString();
	}
}