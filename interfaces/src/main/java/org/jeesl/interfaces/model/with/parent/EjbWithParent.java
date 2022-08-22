package org.jeesl.interfaces.model.with.parent;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithParent extends EjbWithId
{
	public <P extends EjbWithCode> P getParent();
	public <P extends EjbWithCode> void setParent(P parent);
	
	
}

// This can be an alternative implementation ..
//public interface EjbWithCodeParent <P extends EjbWithCode> extends EjbWithId
//{
//	P getParent();
//	void setParent(P parent);
//	
//	void x();
//}