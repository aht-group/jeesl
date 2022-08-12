package org.jeesl.interfaces.model.io.label.revision.core;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbPersistable;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.primitive.position.EjbWithPositionVisible;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslRevisionEntityMapping<RS extends JeeslRevisionScope<?,?,?,?>,
											RST extends JeeslRevisionScopeType<?,?,RST,?>,
											RE extends JeeslRevisionEntity<?,?,?,?,?,?>>
		extends Serializable,EjbPersistable,EjbSaveable,EjbRemoveable,
				EjbWithPositionVisible
{			
	public static enum Type{xpath,jpqlTree}
	
	RS getScope();
	void setScope(RS scope);
	
	RST getType();
	void setType(RST type);
	
	RE getEntity();
	void setEntity(RE entity);
	
	String getXpath();
	void setXpath(String xpath);
	
	String getJpqlTree();
	void setJpqlTree(String jpqlTree);
}