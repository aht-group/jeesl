package org.jeesl.interfaces.model.module.workflow.action;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionAttribute;
import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;

public interface JeeslWorkflowAction <T extends JeeslWorkflowTransition<?,?,?,?,?,?,?>,
										AB extends JeeslWorkflowBot<AB,?,?,?>,
										AO extends EjbWithId,
										RE extends JeeslRevisionEntity<?,?,?,?,RA,?>,
										RA extends JeeslRevisionAttribute<?,?,RE,?,?>
>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithPosition,EjbWithParentAttributeResolver
				
{
	public enum Attributes{transition}
	public enum Constraint{invalidCommand,constraintNotFound,remarkEmpty,missingFile}
	
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