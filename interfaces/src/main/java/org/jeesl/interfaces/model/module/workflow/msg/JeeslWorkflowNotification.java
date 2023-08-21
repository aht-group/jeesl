package org.jeesl.interfaces.model.module.workflow.msg;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.mail.template.JeeslIoTemplate;
import org.jeesl.interfaces.model.io.mail.template.JeeslTemplateChannel;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.security.access.JeeslSecurityRole;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;

public interface JeeslWorkflowNotification <MT extends JeeslIoTemplate<?,?,?,?,?,?>,
											MC extends JeeslTemplateChannel<?,?,MC,?>,
											SR extends JeeslSecurityRole<?,?,?,?,?,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>
									>
		extends Serializable,EjbPersistable,EjbRemoveable,EjbSaveable,
				EjbWithId,EjbWithPosition,EjbWithParentAttributeResolver
				
{	
	MT getTemplate();
	void setTemplate(MT template);
	
	MC getChannel();
	void setChannel(MC channel);
	
	SR getRole();
	void setRole(SR role);
	
	RE getScope();
	void setScope(RE scope);
}