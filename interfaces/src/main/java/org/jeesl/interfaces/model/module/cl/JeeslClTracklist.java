package org.jeesl.interfaces.model.module.cl;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithDateRange;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslClTracklist <L extends JeeslLang,
								R extends JeeslTenantRealm<L,?,R,?>,
								CL extends JeeslClChecklist<L,R,?>>
			extends Serializable,EjbSaveable,EjbRemoveable,
					JeeslWithTenantSupport<R>,
//					JeeslWithTenantTopic<T>,
					EjbWithLang<L>,
					JeeslWithDateRange
					
{
	public enum Attributes{realm,rref}
	
	List<CL> getChecklists();
	void setChecklists(List<CL> checklists);
}