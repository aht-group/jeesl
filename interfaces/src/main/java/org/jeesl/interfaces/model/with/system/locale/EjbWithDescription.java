package org.jeesl.interfaces.model.with.system.locale;

import java.util.Map;

import org.jeesl.interfaces.model.system.locale.JeeslDescription;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;

public interface EjbWithDescription<D extends JeeslDescription> extends EjbWithId
{	
	public Map<String,D> getDescription();
	public void setDescription(Map<String,D> description);
}