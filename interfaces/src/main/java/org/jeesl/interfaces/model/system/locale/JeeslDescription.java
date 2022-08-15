package org.jeesl.interfaces.model.system.locale;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslDescription extends Serializable,EjbWithId,EjbRemoveable,EjbSaveable,EjbPersistable
{
	String getLkey();
	void setLkey(String lkey);
	
	String getLang();
	void setLang(String name);
	
	public Boolean getStyled();
	public void setStyled(Boolean styled);
}