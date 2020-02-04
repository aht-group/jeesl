package org.jeesl.interfaces.model.module.workflow.action;

import java.io.Serializable;

import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.system.io.revision.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.position.EjbWithPosition;

import net.sf.ahtutils.interfaces.model.behaviour.EjbSaveable;
import net.sf.ahtutils.interfaces.model.crud.EjbPersistable;
import net.sf.ahtutils.interfaces.model.crud.EjbRemoveable;
import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslWorkflowAction <T extends JeeslWorkflowTransition<?,?,?,?,?,?>,
										AB extends JeeslWorkflowBot<AB,?,?,?>,
										AO extends EjbWithId,
										RE extends JeeslRevisionEntity<?,?,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<?,?,RE,?,?>
>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithPosition,EjbWithParentAttributeResolver
				
{
	public enum Attributes{transition}
	public enum Constraint{invalidCommand,constraintNotFound,remarkEmpty}
	
	T getTransition();
	void setTransition(T transition);

	AB getBot();
	void setBot(AB bot);
	
	RE getEntity();
	void setEntity(RE entity);
	
	RA getAttribute();
	void setAttribute(RA attribute);
	
	AO getOption();
	void setOption(AO option);
	
	String getCallbackCommand();
	void setCallbackCommand(String callbackCommand);
}