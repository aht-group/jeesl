package org.jeesl.interfaces.cache.module.aom;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.module.aom.event.JeeslAomEventType;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;

public interface JeeslAomEventCache <REALM extends JeeslTenantRealm<?,?,REALM,?>,
									ETYPE extends JeeslAomEventType<?,?,ETYPE,?>>
								extends Serializable
{	
	List<ETYPE> getEventType();
}