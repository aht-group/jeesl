package org.jeesl.interfaces.model.module.hd.ticket;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.cms.markup.JeeslIoMarkup;
import org.jeesl.interfaces.model.io.fr.JeeslFileContainer;
import org.jeesl.interfaces.model.io.fr.JeeslWithFileRepositoryContainer;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.hd.event.JeeslHdEvent;
import org.jeesl.interfaces.model.system.tenant.JeeslTenantRealm;
import org.jeesl.interfaces.model.system.tenant.JeeslWithTenantSupport;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithNonUniqueCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslHdTicket<R extends JeeslTenantRealm<?,?,R,?>,
								EVENT extends JeeslHdEvent<?,?,?,?,?,?,?>,
								M extends JeeslIoMarkup<?>,
								FRC extends JeeslFileContainer<?,?>>
						extends Serializable,EjbSaveable,
								EjbWithId,EjbWithName,EjbWithNonUniqueCode,
								JeeslWithTenantSupport<R>,
								JeeslWithFileRepositoryContainer<FRC>
{	
	public enum Attributes{lastEvent}
	
	EVENT getFirstEvent();
	void setFirstEvent(EVENT firstEvent);
	
	EVENT getLastEvent();
	void setLastEvent(EVENT lastEvent);
	
	M getMarkupUser();
	void setMarkupUser(M markupUser);
	
	M getMarkupSupport();
	void setMarkupSupport(M markupSupport);
	
	M getMarkupSolution();
	void setMarkupSolution(M markupSolution);
}