package org.jeesl.controller.handler.module.workflow;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.beanutils.BeanUtils;
import org.jeesl.api.bean.msg.JeeslConstraintsBean;
import org.jeesl.api.bean.msg.JeeslFacesMessageBean;
import org.jeesl.controller.util.comparator.primitive.BooleanComparator;
import org.jeesl.exception.ejb.JeeslConstraintViolationException;
import org.jeesl.exception.ejb.JeeslLockingException;
import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.exception.processing.JeeslWorkflowException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowActionCallback;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowActionHandler;
import org.jeesl.interfaces.controller.handler.module.workflow.JeeslWorkflowActionsHandler;
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
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWorkflowActionHandler <WPD extends JeeslWorkflowDocument<?,?,?>,
										WT extends JeeslWorkflowTransition<?,?,WPD,?,?,?,?>,
										WA extends JeeslWorkflowAction<?,AB,AO,RE,RA>,
										AB extends JeeslWorkflowBot<AB,?,?,?>,
										AO extends EjbWithId,
										RE extends JeeslRevisionEntity<?,?,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<?,?,RE,?,?>,
										WF extends JeeslWorkflow<?,?,?,?>,
										WC extends JeeslConstraint<?,?,?,?,WC,?,?,?>,
										USER extends JeeslUser<?>>
					implements JeeslWorkflowActionsHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER>
{
	final static Logger logger = LoggerFactory.getLogger(AbstractWorkflowActionHandler.class);
	
	private final String localeCode;
	private boolean debugOnInfo; @Override public void setDebugOnInfo(boolean debugOnInfo) {this.debugOnInfo = debugOnInfo;}
	
	protected final JeeslConstraintsBean<WC> bConstraint;
	protected final JeeslFacesMessageBean bMessage;
	private final JeeslWorkflowActionCallback<WA> callback;

	private final List<JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER>> actionHandlers;

	public AbstractWorkflowActionHandler(String localeCode, JeeslConstraintsBean<WC> bConstraint,
									JeeslFacesMessageBean bMessage,
									JeeslWorkflowActionCallback<WA> callback,
									JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> actionHandler)
	{
		this.localeCode=localeCode;
		this.bConstraint=bConstraint;
		this.bMessage=bMessage;
		this.callback=callback;
		actionHandlers = new ArrayList<>();
		if(actionHandler!=null) {actionHandlers.add(actionHandler);}
	}
	
	@Override public void checkRemark(List<WC> constraints, WT transition, String remark)
	{
		if(debugOnInfo) {logger.info("Checking remark for "+transition.getClass().getSimpleName()+" mandatory:"+transition.getRemarkMandatory());}
		if(BooleanComparator.active(transition.getRemarkMandatory()))
		{
			if(remark==null || remark.trim().isEmpty())
			{
				if(debugOnInfo) {logger.info("Adding a constraint");}
				try{constraints.add(bConstraint.get(JeeslWorkflowAction.class, JeeslWorkflowAction.Constraint.remarkEmpty));}
				catch (JeeslNotFoundException e) {constraints.add(this.getConstraintNotFound());}
			}
		}
	}
	
	@Override public <W extends JeeslWithWorkflow<WF>> JeeslWithWorkflow<WF> perform(USER user, WT transition, JeeslWithWorkflow<WF> entity, List<WA> actions) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException, UtilsProcessingException, JeeslWorkflowException
	{
		if(debugOnInfo) {logger.info("Performing Actions "+entity.toString());}
		for(WA action : actions)
		{
			entity = perform(user,entity,action);
		}
		if(Objects.nonNull(callback)) {callback.workflowCallback(entity);}
		if(bMessage!=null && transition!=null && transition.getConfirmation()!=null && transition.getConfirmation().containsKey(localeCode) && !transition.getConfirmation().get(localeCode).getLang().trim().isEmpty())
		{
			bMessage.growlInfoText(transition.getConfirmation().get(localeCode).getLang());
		}
		return entity;
	}
	
	protected <W extends JeeslWithWorkflow<WF>> JeeslWithWorkflow<WF> perform(USER user, JeeslWithWorkflow<WF> entity, WA action) throws JeeslConstraintViolationException, JeeslLockingException, JeeslNotFoundException, UtilsProcessingException, JeeslWorkflowException
	{
		if(debugOnInfo) {logger.info("Perform "+action.toString());}
		
		if(action.getBot().getCode().contentEquals("statusUpdate"))
		{
			statusUpdate(entity,action);
			for(JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> ah : actionHandlers)
			{
				entity = ah.statusUpdated(entity);
			}
		}
		else if(action.getBot().getCode().contentEquals("callbackCommand"))
		{
			for(JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> ah : actionHandlers)
			{
				entity = ah.perform(user,entity,action);
			}
		}
		else if(action.getBot().getCode().contentEquals("appendRandomInt")) {appendRandomInt(entity,action);}
		else {logger.warn("Unknown Bot");}
		
		
		return entity;
		
	}
	
	private <W extends JeeslWithWorkflow<WF>> void statusUpdate(JeeslWithWorkflow<WF> entity, WA action)
	{
		if(debugOnInfo) {logger.info("statusUpdate "+entity.toString()+" option:"+action.getOption().toString());}
		try
		{
			BeanUtils.setProperty(entity,action.getAttribute().getCode(),action.getOption());
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
	
	private <W extends JeeslWithWorkflow<WF>> void appendRandomInt(JeeslWithWorkflow<WF> entity, WA action)
	{
		if(entity instanceof EjbWithName)
		{
			Random rnd = new Random();
			EjbWithName ejb = (EjbWithName)entity;
			ejb.setName(ejb.getName()+" "+rnd.nextInt(10));
		}
	}

	@Override public <W extends JeeslWithWorkflow<WF>> void abort(JeeslWithWorkflow<WF> entity)
	{
		try
		{
			callback.workflowAbort(entity);
		}
		catch (JeeslConstraintViolationException | JeeslLockingException | JeeslNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override public boolean checkVeto(JeeslWithWorkflow<?> entity, WT transition)
	{
		boolean veto = false;
		for(JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> ah : actionHandlers)
		{
			if(ah.checkVeto(entity,transition)) {veto=true;}
		}
		return veto;
	}
	
	@Override public void checkPreconditions(List<WC> constraints, JeeslWithWorkflow<?> entity, WT transition)
	{
		for(JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> ah : actionHandlers)
		{
			try
			{
				ah.workflowPreconditions(constraints, entity, transition);
			}
			catch (JeeslNotFoundException e)
			{
				WC c = getConstraintNotFound();
				c.setContextMessage(e.getMessage());
				constraints.add(c);
			}
		}
	}
	
	@Override public void checkPreconditions(List<WC> constraints, JeeslWithWorkflow<?> entity, List<WA> actions)
	{
		for(JeeslWorkflowActionHandler<WPD,WT,WA,AB,AO,RE,RA,WF,WC,USER> ah : actionHandlers)
		{
			for(WA action : actions)
			{
				try
				{
					ah.workflowPreconditions(constraints,entity, action);
				}
				catch (JeeslNotFoundException e)
				{
					WC c = getConstraintNotFound();
					c.setContextMessage(e.getMessage());
					constraints.add(c);
				}
			}
		}
	}
	
	protected abstract WC getConstraintNotFound();
}