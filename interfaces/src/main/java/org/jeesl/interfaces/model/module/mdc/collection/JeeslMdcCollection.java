package org.jeesl.interfaces.model.module.mdc.collection;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.attribute.JeeslAttributeSet;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.date.ju.EjbWithValidFromUntil;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;

public interface JeeslMdcCollection <R extends JeeslTenantRealm<?,?,R,?>,
									
									SCOPE extends JeeslMdcScope<?,?,R,SCOPE,?>,
									STATUS extends JeeslMdcStatus<?,?,STATUS,?>,
									AS extends JeeslAttributeSet<?,?,?,?,?,?>
									>
		extends Serializable,EjbSaveable,EjbRemoveable,
				EjbWithName,
				JeeslWithTenantSupport<R>,EjbWithValidFromUntil
				
{
	public enum Attributes{scope, attributeSet}
	
	SCOPE getScope();
	void setScope(SCOPE scope);
	
	STATUS getStatus();
	void setStatus(STATUS status);
	
	AS getCollectionSet();
	void setCollectionSet(AS collectionSet);
}