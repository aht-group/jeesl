package org.jeesl.interfaces.model.system.locale;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;

import net.sf.ahtutils.model.interfaces.with.EjbWithId;

public interface JeeslDescription extends EjbWithId,EjbRemoveable,EjbSaveable
{
	String getLkey();
	void setLkey(String lkey);
	
	String getLang();
	void setLang(String name);
	
	public Boolean getStyled();
	public void setStyled(Boolean styled);
}