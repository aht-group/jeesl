package org.jeesl.interfaces.model.system.security.user;

import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslSecurityUser extends EjbWithId//<R extends JeeslSecurityRole<?,?,?,?,?,?>>
{
	public enum Fm {fmPwdChanged}
//	void x();
}