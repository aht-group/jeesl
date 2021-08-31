package org.jeesl.interfaces.model.module.am;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;

public interface JeeslAmActivity <L extends JeeslLang, D extends JeeslDescription,
							REALM extends JeeslTenantRealm<?,?,REALM,?>,
							ACTIVITY extends JeeslAmActivity<L,D,REALM,ACTIVITY>>
			extends Serializable,EjbSaveable,
					EjbWithPosition,EjbWithParentAttributeResolver,
					EjbWithLang<L>,EjbWithDescription<D>,
					EjbWithNonUniqueCode
					
{
	public enum Attributes{realm,parent}
	
	// Will be implemented later
//	REALM getRealm();
//	void setRealm(REALM realm);
//	
//	long getRealmIdentifier();
//	void setRealmIdentifier(long realmIdentifier);
	
	ACTIVITY getParent();
	void setParent(ACTIVITY parent);

}