package org.jeesl.interfaces.model.module.workflow.instance;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.workflow.process.JeeslWorkflowProcess;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.system.security.user.JeeslUser;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface JeeslWorkflow <WP extends JeeslWorkflowProcess<?,?,?,WS>,
								WS extends JeeslWorkflowStage<?,?,WP,?,?,?,?>,
								WY extends JeeslWorkflowActivity<?,?,?,?,USER>,
								USER extends JeeslUser<?>		
									>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithParentAttributeResolver
{
	public static enum Attributes{id,process,currentStage,lastActivity,responsibles}
	
	WP getProcess();
	void setProcess(WP process);
	
	WS getCurrentStage();
	void setCurrentStage(WS currentStage);
	
	List<WY> getActivities();
	void setActivities(List<WY> activities);
	
	WY getLastActivity();
	void setLastActivity(WY lastActivity);
	
	List<USER> getResponsibles();
	void setResponsibles(List<USER> responsibles);
}