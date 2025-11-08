package org.jeesl.interfaces.model.system.property;

import java.io.Serializable;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.system.locale.JeeslLang;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.interfaces.model.with.system.locale.EjbWithDescription;
import org.jeesl.interfaces.model.with.system.locale.EjbWithLang;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslProperty <L extends JeeslLang, D extends JeeslDescription,
								C extends JeeslStatus<L,D,C>,
								P extends JeeslProperty<L,D,C,P>>
		extends Serializable,EjbSaveable,EjbRemoveable,EjbWithLang<L>,EjbWithDescription<D>
{
	public enum Code{autoLogin}
	
	C getCategory();
	void setCategory(C category);
	
	String getKey();
	void setKey(String key);
	
	String getValue();
	void setValue(String value);
	
	boolean isFrozen();
	void setFrozen(boolean frozen);
	
	Integer getPosition();
	void setPosition(Integer position);
	
	Boolean getDocumentation();
	void setDocumentation(Boolean documentation);
}