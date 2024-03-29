package org.jeesl.interfaces.model.module.rmmv;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslRmmvSubscriptionItem<SUB extends JeeslRmmvSubscription<?,?,?>,
								MC extends JeeslRmmvModuleConfig<?,?>>
						extends Serializable,EjbSaveable,EjbRemoveable,
								EjbWithPositionVisible,EjbWithParentAttributeResolver
{
	public enum Attributes{subscription}
	
	SUB getSubscription();
	void setSubscription(SUB subscription);
	
	MC getConfig();
	void setConfig(MC config);
}