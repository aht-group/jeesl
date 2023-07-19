package org.jeesl.interfaces.model.io.ssi.data;

import org.jeesl.interfaces.model.io.label.entity.JeeslRevisionEntity;
import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslAttributes;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
@DownloadJeeslAttributes
public interface JeeslIoSsiAttribute <MAPPING extends JeeslIoSsiContext<?,ENTITY>,
									 ENTITY extends JeeslRevisionEntity<?,?,?,?,?,?>>
		extends EjbWithId,EjbSaveable,EjbRemoveable,
				EjbWithParentAttributeResolver
{	
	public enum Attributes{mapping,entity}
	
	public MAPPING getMapping();
	public void setMapping(MAPPING mapping);
	
	public ENTITY getEntity();
	public void setEntity(ENTITY entity);
	
	public String getLocalCode();
	public void setLocalCode(String code);
	
	public String getRemoteCode();
	public void setRemoteCode(String code);
}