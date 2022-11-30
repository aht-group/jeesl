package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvSubscription<R extends JeeslTenantRealm<?,?,R,?>,
								MOD extends JeeslRmmvModule<?,?,MOD,?>,
								USER extends EjbWithId>
						extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithCode,EjbWithPositionVisible,EjbWithName,
								JeeslWithTenantSupport<R>
{
	public enum Attributes{user}
	
	MOD getModule();
	void setModule(MOD module);
	
	USER getUser();
	void setUser(USER user);
}