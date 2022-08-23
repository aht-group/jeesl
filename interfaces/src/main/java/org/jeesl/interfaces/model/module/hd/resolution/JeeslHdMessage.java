package org.jeesl.interfaces.model.module.hd.resolution;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.module.hd.ticket.JeeslHdTicket;
import org.jeesl.interfaces.model.system.locale.JeeslMarkup;
import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.interfaces.model.with.date.ju.EjbWithRecord;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.system.status.JeeslWithScope;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslHdMessage<TICKET extends JeeslHdTicket<?,?,?,?>,
								M extends JeeslMarkup<?>,
								SCOPE extends JeeslHdScope<?,?,SCOPE,?>,
								USER extends JeeslSimpleUser>
						extends Serializable,EjbSaveable,
								EjbWithId,
								EjbWithParentAttributeResolver,
								EjbWithRecord,
								JeeslWithScope<SCOPE>
{	
	public enum Attributes{ticket,author,scope}
	
	TICKET getTicket();
	void setTicket(TICKET ticket);
	
	USER getUser();
	void setUser(USER user);
	
	M getMarkup();
	void setMarkup(M markup);
}