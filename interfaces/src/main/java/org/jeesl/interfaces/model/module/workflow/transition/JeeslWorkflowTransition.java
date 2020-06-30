package org.jeesl.interfaces.model.module.workflow.transition;

import java.io.Serializable;
import java.util.Map;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.workflow.stage.JeeslWorkflowStage;
import org.jeesl.interfaces.model.system.graphic.core.JeeslGraphic;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.security.framework.JeeslSecurityRole;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslWorkflowTransition <L extends JeeslLang, D extends JeeslDescription,
									S extends JeeslWorkflowStage<L,D,?,?,?,?,?>,
									WTT extends JeeslWorkflowTransitionType<L,D,WTT,?>,
									SR extends JeeslSecurityRole<L,D,?,?,?,?,?>,
									G extends JeeslGraphic<L,D,?,?,?>
									>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithPosition,EjbWithParentAttributeResolver,
				EjbWithLang<L>,EjbWithDescription<D>,
				EjbWithVisible
{
	public static enum Attributes{source,destination}
	
	WTT getType();
	void setType(WTT type);
	
	S getSource();
	void setSource(S source);
	
	S getDestination();
	void setDestination(S destination);
	
	SR getRole();
	void setRole(SR role);
	
	Boolean getScreenSignature();
	void setScreenSignature(Boolean screenSignature);
	
	Boolean getRemarkMandatory();
	void setRemarkMandatory(Boolean remarkMandatory);
	
	Boolean getFileUpload();
	void setFileUpload(Boolean fileUpload);
	
	Map<String,D> getConfirmation();
	void setConfirmation(Map<String,D> confirmation);
	
	Integer getAutoTransitionTimer();
	void setAutoTransitionTimer(Integer autoTransitionTimer);
}