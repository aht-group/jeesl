package org.jeesl.interfaces.model.system.tenant;

import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithStyle;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithSymbol;

public interface JeeslTenantStatus <REALM extends JeeslTenantRealm<?,?,REALM,?>>
						extends JeeslWithTenantSupport<REALM>,
									EjbWithPositionVisible,EjbWithStyle,EjbWithCode,EjbWithSymbol
{	
//		void x();
}