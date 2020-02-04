package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.status.JeeslWithType;

import net.sf.ahtutils.interfaces.model.with.parent.EjbWithParentAttributeResolver;

public interface JeeslWithParentAttributeType <TYPE extends JeeslStatus<TYPE,?,?>> 
								extends EjbWithParentAttributeResolver,JeeslWithType<TYPE>
{


}