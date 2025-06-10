package org.jeesl.interfaces.model.io.ssi.data;

import java.io.Serializable;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.io.ssi.core.JeeslIoSsiSystem;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiContext <SYSTEM extends JeeslIoSsiSystem<?,?>,
									ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
		extends Serializable,EjbWithId,EjbSaveable,EjbRemoveable,EjbWithParentAttributeResolver
{	
	public enum Attributes {system,entity,json}
	
	public SYSTEM getSystem();
	public void setSystem(SYSTEM system);
	
	public ENTITY getEntity();
	public void setEntity(ENTITY entity);
	
	public ENTITY getJson();
	public void setJson(ENTITY json);
	
	
	public ENTITY getClassA();
	public void setClassA(ENTITY classA);
	
	public ENTITY getClassB();
	public void setClassB(ENTITY classB);
	
	public ENTITY getClassC();
	public void setClassC(ENTITY classC);
	
	String getName();
	void setName(String name);
}