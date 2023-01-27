package org.jeesl.api.bean.msg;

import org.jeesl.exception.ejb.JeeslNotFoundException;
import org.jeesl.interfaces.model.system.constraint.core.JeeslConstraint;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;

import net.sf.ahtutils.xml.system.ConstraintScope;

public interface JeeslConstraintsBean <CONSTRAINT extends JeeslConstraint<?,?,?,?,CONSTRAINT,?,?,?>>
{
	String getMessage(String category, String scope, String code, String lang);
	ConstraintScope getScope(String category, String scope, String lang);
	
//	<SID extends Enum<SID>, CID extends Enum<CID>> CONSTRAINT get(SID sId, CID cId) throws UtilsNotFoundException;
	<SID extends Enum<SID>, CID extends Enum<CID>> CONSTRAINT getSilent(SID sId, CID cId);
	<CID extends Enum<CID>> CONSTRAINT get(Class<?> cScope, CID cId) throws JeeslNotFoundException;
	<CID extends Enum<CID>> CONSTRAINT getSilent(Class<?> cScope, CID cId);
	<S extends JeeslStatus<?,?,S>> CONSTRAINT getSilent(Class<?> cScope, S status);
	void update(CONSTRAINT constraint);
	void ping();
}