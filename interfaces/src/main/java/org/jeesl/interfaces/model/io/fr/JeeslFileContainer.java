package org.jeesl.interfaces.model.io.fr;

import java.io.Serializable;
import java.util.List;

import org.jeesl.interfaces.model.marker.jpa.EjbRemoveable;
import org.jeesl.interfaces.model.marker.jpa.EjbSaveable;
import org.jeesl.interfaces.model.with.parent.EjbWithParentAttributeResolver;
import org.jeesl.interfaces.model.with.primitive.number.EjbWithId;
import org.jeesl.interfaces.qualifier.rest.option.DownloadJeeslDescription;

@DownloadJeeslDescription
public interface JeeslFileContainer<STORAGE extends JeeslFileStorage<?,?,?,?,?>,
									META extends JeeslFileMeta<?,?,?,?>>
		extends Serializable,EjbWithId,
					EjbSaveable,EjbRemoveable,
					EjbWithParentAttributeResolver
{
	public enum Attributes{storage,metas}
	
	STORAGE getStorage();
	void setStorage(STORAGE storage);
	
	List<META> getMetas();
	void setMetas(List<META> metas);
}