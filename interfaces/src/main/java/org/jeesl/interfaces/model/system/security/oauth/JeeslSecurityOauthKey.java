package org.jeesl.interfaces.model.system.security.oauth;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.date.jt.JeeslWithRecordDateTime;
import org.jeesl.interfaces.model.with.primitive.bool.EjbWithVisible;
import org.jeesl.interfaces.model.with.primitive.code.EjbWithCode;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.model.with.primitive.text.EjbWithName;
import org.jeesl.interfaces.model.with.system.status.JeeslWithType;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslAttributes
@DownloadJeeslDescription
public interface JeeslSecurityOauthKey <TYPE extends JeeslSecurityOauthKeyType<?,?,TYPE,?>>
			extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,
					JeeslWithRecordDateTime,EjbWithCode,EjbWithVisible,
					EjbWithName,JeeslWithType<TYPE>
{
	public enum Attributes{user,type}
	
	
	
	String getJson();
	void setJson(String json);
}