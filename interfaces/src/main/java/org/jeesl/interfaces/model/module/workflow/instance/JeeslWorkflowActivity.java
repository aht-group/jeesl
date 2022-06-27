package org.jeesl.interfaces.model.module.workflow.instance;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.workflow.transition.JeeslWorkflowTransition;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWorkflowActivity <WT extends JeeslWorkflowTransition<?,?,?,?,?,?,?>,
										WF extends JeeslWorkflow<?,?,?,USER>,
										WD extends JeeslWorkflowDelegate<?,USER>,
										FRC extends JeeslFileContainer<?,?>,
										USER extends JeeslUser<?>
									>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithParentAttributeResolver,
				EjbWithRecord,JeeslWithFileRepositoryContainer<FRC>
{
	public static enum Attributes{workflow,delegate,user,record}
	
	WF getWorkflow();
	void setWorkflow(WF workflow);
	
	WT getTransition();
	void setTransition(WT transition);
	
	USER getUser();
	void setUser(USER user);
	
	String getRemark();
	void setRemark(String remark);
	
	String getScreenSignature();
	void setScreenSignature(String screenSignature);
	
	WD getDelegate();
	void setDelegate(WD delegate);
}