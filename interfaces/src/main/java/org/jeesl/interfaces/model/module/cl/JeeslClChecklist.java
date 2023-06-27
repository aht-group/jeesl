package org.jeesl.interfaces.model.module.cl;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPosition;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.model.with.system.tenant.JeeslWithTenantTopic;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslClChecklist <L extends JeeslLang,
								R extends JeeslTenantRealm<L,?,R,?>,
								TO extends JeeslClCategory<L,?,R,TO,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslWithTenantSupport<R>,
					EjbWithPosition,
					JeeslWithTenantTopic<TO>,
					EjbWithLang<L>
					
{
	public enum Attributes{realm,rref,topic}
	
	
}