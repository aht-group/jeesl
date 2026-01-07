package org.jeesl.interfaces.model.module.workflow.instance;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithRefId;

public interface JeeslWorkflowLink <WF extends JeeslWorkflow<?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>
									>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithRefId,EjbWithParentAttributeResolver
{
	public static enum Attributes{id,refId,workflow,entity}
	
	WF getWorkflow();
	void setWorkflow(WF workflow);
	
	RE getEntity();
	void setEntity(RE entity);
}