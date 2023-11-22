package org.jeesl.interfaces.controller.handler.module.workflow;

import java.util.List;

import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.JeeslWorkflowException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowAction;
import org.jeesl.interfaces.model.module.workflow.action.JeeslWorkflowBot;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWithWorkflow;
import org.jeesl.interfaces.model.module.workflow.instance.JeeslWorkflow;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowDocument;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWorkflowActionsHandler<WPD extends JeeslWorkflowDocument<?,?,?>,
											WT extends JeeslWorkflowTransition<?,?,WPD,?,?,?,?>,
											WA extends JeeslWorkflowAction<?,AB,AO,RE,RA>,
											AB extends JeeslWorkflowBot<AB,?,?,?>,
											AO extends EjbWithId,
											RE extends JeeslRevisionEntity<?,?,?,?,RA,?>,
											RA extends JeeslRevisionAttribute<?,?,RE,?,?>,
											WF extends JeeslWorkflow<?,?,?,?>,
											WCS extends JeeslConstraint<?,?,?,?,?,?,?,?>,
											USER extends JeeslUser<?>>
{
	void setDebugOnInfo(boolean debugOnInfo);
	
	void checkRemark(List<WCS> constraints, WT transition, String remark);
	boolean checkVeto(JeeslWithWorkflow<?> entity, WT transition);
	void checkPreconditions(List<WCS> constraints, JeeslWithWorkflow<?> entity, WT transition);
	void checkPreconditions(List<WCS> constraints, JeeslWithWorkflow<?> entity, List<WA> actions);
	void checkDocuments(List<WCS> constraints, JeeslWithWorkflow<?> entity, List<WPD> documents);
	<W extends JeeslWithWorkflow<WF>> void abort(JeeslWithWorkflow<WF> entity);
	<W extends JeeslWithWorkflow<WF>> JeeslWithWorkflow<WF> perform(USER user, WT transition, JeeslWithWorkflow<WF> entity, List<WA> actions) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException, UtilsProcessingException, JeeslWorkflowException;
}