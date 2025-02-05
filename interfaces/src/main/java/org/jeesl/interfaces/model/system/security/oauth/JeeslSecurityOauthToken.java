package org.jeesl.interfaces.model.system.security.oauth;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeeslSecurityOauthToken 
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					EjbWithCode
{
	public enum Attributes{user,type}
	
	

}