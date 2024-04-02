package org.jeesl.interfaces.model.module.its.issue;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.parent.EjbWithParentId;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.status.JeeslWithStatus;

public interface JeeslItsIssue <R extends JeeslTenantRealm<?,?,R,?>,
								I extends JeeslItsIssue<R,I,IS>,
								IS extends JeeslItsIssueStatus<?,?,R,IS,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslWithTenantSupport<R>,
					EjbWithParentAttributeResolver, EjbWithParentId<I>,
					EjbWithNonUniqueCode,EjbWithPosition,
					EjbWithName,JeeslWithStatus<IS>
					
{
	public enum Attributes{realm,rref,parent}
	
	I getParent();
	void setParent(I parent);
	
	List<I> getIssues();
	void setIssues(List<I> types);
}